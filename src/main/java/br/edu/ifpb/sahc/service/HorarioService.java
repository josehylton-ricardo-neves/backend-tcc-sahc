package br.edu.ifpb.sahc.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifpb.sahc.dto.horario.HorarioResponse;
import br.edu.ifpb.sahc.dto.horario.Horarios;
import br.edu.ifpb.sahc.model.Coordenacao;
import br.edu.ifpb.sahc.model.Horario;
import br.edu.ifpb.sahc.model.Usuario;
import br.edu.ifpb.sahc.repository.HorarioRepository;

@Service
public class HorarioService {

	@Autowired
	private HorarioRepository horarioRepository;
	@Autowired
	private UsuarioService usuarioService;
	
	
	public List<Horarios> solicitarHorarioAgendado(String matricula) {
		Optional<Usuario> usuario = usuarioService.findByMatricula(matricula);
		Coordenacao coordenacao = usuario.get().getIdCoordenacao();
		
		List<Horarios> horarios = new ArrayList<>();
		if(usuario.get().getRole()=="aluno"){
			List<Optional<Horario>> horarioAgendado = horarioRepository.findAllByIdCoordenacaoAndMatriculaUsuario(coordenacao, usuario);

			horarioAgendado.forEach(e -> horarios.add(new Horarios(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(e.get().getHoraInicial()),
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(e.get().getHoraFinal())
			)));
		}else{
			List<Optional<Horario>> horarioAgendado = horarioRepository.findAllByIdCoordenacaoAndHorarioAgendado(coordenacao,true);

			horarioAgendado.forEach(e -> horarios.add(new Horarios(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(e.get().getHoraInicial()),
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(e.get().getHoraFinal())
			)));
		}

		return horarios;
	}
	

	public List<Horarios> solicitarHorarioDisponivel(String matricula) {
		Coordenacao coordenacao = usuarioService.findByMatricula(usuarioService.adquireUsuario(matricula)).get().getIdCoordenacao();
		List<Optional<Horario>> horarioDisponivel = horarioRepository.findAllByIdCoordenacao(coordenacao);
		List<Horarios> horarios = new ArrayList<>();
		horarioDisponivel.forEach(e -> horarios.add(new Horarios(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(e.get().getHoraInicial()), 
																 new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(e.get().getHoraFinal())
								  )));

		return horarios;
	}
	

	public ResponseEntity<HorarioResponse> agendarHorarioDisponivel(String horario, String matricula) {
		List<Horarios> horarios = mapearHorarios(horario);

		Optional<Usuario> usuario = usuarioService.findByMatricula(matricula);
		Coordenacao coordenacao = usuario.get().getIdCoordenacao();
		Timestamp horaInicial = Timestamp.valueOf(horarios.get(0).getHoraInicial());
		Timestamp horaFinal = Timestamp.valueOf(horarios.get(0).getHoraFinal());
				
		Optional<Horario> verificaDisponibilidade = horarioRepository.findByIdCoordenacaoAndHoraInicialAndHoraFinalAndHorarioAgendado(coordenacao, horaInicial, horaFinal, false);
		
		if(verificaDisponibilidade.isEmpty()) {
			return responseEntity(404, "O horario escolhido não está disponivel", HttpStatus.NOT_FOUND);
		}
		horarioRepository.agendarHorarioDisponivel(coordenacao, horaInicial, horaFinal, usuario);
		return responseEntity(200, "Horario agendado com sucesso", HttpStatus.OK);
	}
	
	

	public ResponseEntity<HorarioResponse> desagendarHorarioDisponivel(String horario, String matricula) {
		List<Horarios> horarios = mapearHorarios(horario);

		Optional<Usuario> usuario = usuarioService.findByMatricula(matricula);
		Coordenacao coordenacao = usuario.get().getIdCoordenacao();
		Timestamp horaInicial = Timestamp.valueOf(horarios.get(0).getHoraInicial());
		Timestamp horaFinal = Timestamp.valueOf(horarios.get(0).getHoraFinal());

		Optional<Horario> verificaHorarioAgendado = horarioRepository.findByIdCoordenacaoAndHoraInicialAndHoraFinalAndMatriculaUsuarioAndHorarioAgendado(coordenacao,
				horaInicial, horaFinal, usuario, true);
	
		if(verificaHorarioAgendado.isEmpty()) {
			return responseEntity(404, "Informe um horario no qual voce está agendado", HttpStatus.NOT_FOUND);
		}
		
		horarioRepository.desagendarHorarioDisponivel(coordenacao, horaInicial, horaFinal, usuario);
		return responseEntity(200, "Horario desagendado com sucesso", HttpStatus.OK);
	}
	
	
	public List<Optional<Horario>> findHorariosConflitantes (Coordenacao idCoordenacao, Timestamp horaInicial, Timestamp horaFinal) {
		return horarioRepository.findHorariosConflitantes(idCoordenacao, horaInicial, horaFinal);
	}
	
	public void save(Horario horario) {
		horarioRepository.save(horario);
	}
	
	public int apagarHorarioCadastrado(Coordenacao idCoordenacao, Timestamp horaInicial, Timestamp horaFinal) {
		return horarioRepository.apagarHorarioCadastrado(idCoordenacao, horaInicial, horaFinal);
	}
	
	private List<Horarios> mapearHorarios(String horarios) {
		ObjectMapper mapper = new ObjectMapper();
		List<Horarios> listaHorarios = null;
		
		try {
			listaHorarios = Arrays.asList(mapper.readValue(horarios, Horarios[].class));
		} catch (JsonMappingException e) {
			System.out.println("Erro 1");
		} catch (JsonProcessingException e) {
			System.out.println("Erro 2");
		}
		return listaHorarios;
	}
	
	public void deleteAllByIdCoordenacao(Coordenacao coordenacao) {
		horarioRepository.deleteAllByIdCoordenacao(coordenacao);
	}
	
	
	private ResponseEntity<HorarioResponse> responseEntity(Integer status, String mensagem, HttpStatus httpStatus) {
		
		if(status != null && mensagem != null) {
			HorarioResponse horarioResponse = new HorarioResponse();
			horarioResponse.setTimestamp(new java.util.Date());
			horarioResponse.setStatus(status);
			horarioResponse.setMensagem(mensagem);
			return new ResponseEntity<HorarioResponse>(horarioResponse, httpStatus);
		}
		return new ResponseEntity<HorarioResponse>(httpStatus);
	}


}
