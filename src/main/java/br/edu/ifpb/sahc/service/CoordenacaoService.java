package br.edu.ifpb.sahc.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifpb.sahc.dto.admin.CrudCoordenacao;
import br.edu.ifpb.sahc.dto.coordenacao.CoordenacaoResponse;
import br.edu.ifpb.sahc.dto.horario.Horarios;
import br.edu.ifpb.sahc.model.Coordenacao;
import br.edu.ifpb.sahc.model.Horario;
import br.edu.ifpb.sahc.model.HorarioID;
import br.edu.ifpb.sahc.repository.CoordenacaoRepository;

@Service
public class CoordenacaoService {

	@Autowired
	private CoordenacaoRepository coordenacaoRepository;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private HorarioService horarioService;
	
	
	public List<Coordenacao> consultarCoordenacao() {
		return coordenacaoRepository.findAll();
	}
	
	public ResponseEntity<CoordenacaoResponse> cadastrarHorarioDisponivel(String horarios, String matricula) {

		List<Horarios> listaHorario = mapearHorarios(horarios);
		try {

			listaHorario.forEach(e -> {
				if(!horarioService.findHorariosConflitantes(usuarioService.findByMatricula(matricula).get().getIdCoordenacao(), Timestamp.valueOf(e.getHoraInicial()), Timestamp.valueOf(e.getHoraFinal())).isEmpty()) {
					throw new DataIntegrityViolationException("Existe conflito de horarios. Verifique e tente novamente");
				}
			;});
		
		}catch (DataIntegrityViolationException e) {
			return responseEntity(409, e.getMessage(), HttpStatus.CONFLICT);
		}catch (NullPointerException e2) {
			return responseEntity(400, e2.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		listaHorario.forEach(e -> inserirHorario(e, matricula));
		return responseEntity(201, "Horario(s) cadastrado(s) com sucesso", HttpStatus.CREATED);
	}
	
	private void inserirHorario(Horarios horarios, String matricula) {
		Horario horario = new Horario();
		Coordenacao coordenacao = usuarioService.findByMatricula(matricula).get().getIdCoordenacao();
		
		HorarioID horarioId = new HorarioID(coordenacao.getIdCoordenacao(), Timestamp.valueOf(horarios.getHoraInicial()), Timestamp.valueOf(horarios.getHoraFinal()));
		horario.setHorarioId(horarioId);
		horario.setIdCoordenacao(coordenacao);
		horario.setHorarioAgendado(false);
		horarioService.save(horario);
		
	}
	
	
	public ResponseEntity<CoordenacaoResponse> descadastrarHorarioDisponivel(String horarios, String matricula) {
		List<Horarios> listaHorarios = mapearHorarios(horarios);
		listaHorarios.stream().forEach(e -> apagarHorario(e, matricula));
		return responseEntity(200, "Horario(s) descadastrado(s) com sucesso", HttpStatus.OK);
	}
	
	
	public void save(Coordenacao coordenacao) {
		coordenacaoRepository.save(coordenacao);
	}
	
	public void delete(Coordenacao coordenacao) {
		coordenacaoRepository.delete(coordenacao);
	}
	
	public Coordenacao getById(Long idCoordenacao) {
		return coordenacaoRepository.getById(idCoordenacao);
	}
	
	public int updateById(CrudCoordenacao coordenacao) {
		return coordenacaoRepository.updateById(coordenacao.getIdCoordenacao(), coordenacao.getNomeCoordenacao(), 
				  coordenacao.getEmailCoordenacao(), coordenacao.getMatriculaCoordenador(),
				  coordenacao.getNomeCoordenador());
	}
	
	
	private void apagarHorario(Horarios horarios, String matricula) {
		Coordenacao idCoordenacao = usuarioService.findByMatricula(matricula).get().getIdCoordenacao();
		
		horarioService.apagarHorarioCadastrado(idCoordenacao, Timestamp.valueOf(horarios.getHoraInicial()), Timestamp.valueOf(horarios.getHoraFinal()));
	}
	
	
	private List<Horarios> mapearHorarios(String horarios) {
		ObjectMapper mapper = new ObjectMapper();
		List<Horarios> listaHorarios = null;
		try {
			listaHorarios = Arrays.asList(mapper.readValue(horarios, Horarios[].class));
		} catch (JsonProcessingException e) {
		}
		return listaHorarios;
	}
	
	
	private ResponseEntity<CoordenacaoResponse> responseEntity(Integer status, String mensagem, HttpStatus httpStatus) {
		
		if(status != null && mensagem != null) {
			CoordenacaoResponse coordenacaoResponse = new CoordenacaoResponse();
			coordenacaoResponse.setTimestamp(new Date());
			coordenacaoResponse.setStatus(status);
			coordenacaoResponse.setMensagem(mensagem);
			return new ResponseEntity<CoordenacaoResponse>(coordenacaoResponse, httpStatus);
		}
		return new ResponseEntity<CoordenacaoResponse>(httpStatus);
	}
	
}
