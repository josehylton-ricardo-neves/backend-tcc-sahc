package br.edu.ifpb.sahc.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.edu.ifpb.sahc.dto.HorarioResponse;
import br.edu.ifpb.sahc.dto.UsuarioResponse;
import br.edu.ifpb.sahc.model.HorarioModel;
import br.edu.ifpb.sahc.repository.HorarioRepository;

public class HorarioService {

	@Autowired
	private HorarioRepository horarioRepository;
	
	public Optional<List<HorarioModel>> solicitarHorarioDisponivel(String nomeCoordenacao) {
		
		Optional<List<HorarioModel>> horarioDisponivel = horarioRepository.findAllByNomeCoordenacao(nomeCoordenacao);
		
		return horarioDisponivel;
	}
	
	public ResponseEntity<HorarioResponse> agendarHorarioDisponivel(String nomeCoordenacao, String horarioDisponivel, boolean horarioAgendado, String matriculaDiscente) {
		
	
		
		
		return responseEntity(null, null, HttpStatus.OK);
	}
	
	
	
	
private ResponseEntity<HorarioResponse> responseEntity(Integer status, String mensagem, HttpStatus httpStatus) {
		
		if(status != null && mensagem != null) {
			UsuarioResponse usuarioResponse = new UsuarioResponse();
			usuarioResponse.setTimestamp(new Date());
			usuarioResponse.setStatus(status);
			usuarioResponse.setMensagem(mensagem);
			return new ResponseEntity<HorarioResponse>(usuarioResponse, httpStatus);
		}
		return new ResponseEntity<HorarioResponse>(httpStatus);
	}

}
