package br.edu.ifpb.sahc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.sahc.dto.horario.HorarioResponse;
import br.edu.ifpb.sahc.dto.horario.Horarios;
import br.edu.ifpb.sahc.service.HorarioService;
import br.edu.ifpb.sahc.service.UsuarioService;

@RestController
public class HorarioController {
	
	@Autowired
	private HorarioService horarioService;
	@Autowired
	private UsuarioService usuarioService;

	
	@GetMapping("/solicitarHorarioAgendado")
	public List<Horarios> solicitarHorarioAgendado(@RequestHeader(value = "Authorization") String token) {
		return horarioService.solicitarHorarioAgendado(usuarioService.adquireUsuario(token));
	}
	
	@GetMapping("/solicitarHorarioDisponivel")
	public List<Horarios> solicitarHorarioDisponivel(@RequestHeader(value = "Authorization") String token) {
		return horarioService.solicitarHorarioDisponivel(usuarioService.adquireUsuario(token));
	}
	
	@PostMapping("/agendarHorarioDisponivel")
	public ResponseEntity<HorarioResponse> agendarHorarioDisponivel(@RequestBody String horarios, @RequestHeader(value = "Authorization") String token) {
		return horarioService.agendarHorarioDisponivel(horarios, usuarioService.adquireUsuario(token));
	}
	
	@PatchMapping("/desagendarHorarioDisponivel")
	public ResponseEntity<HorarioResponse> desagendarHorarioDisponivel(@RequestBody String horario, @RequestHeader(value = "Authorization") String token) {
		return horarioService.desagendarHorarioDisponivel(horario, usuarioService.adquireUsuario(token));
	}
	
}
