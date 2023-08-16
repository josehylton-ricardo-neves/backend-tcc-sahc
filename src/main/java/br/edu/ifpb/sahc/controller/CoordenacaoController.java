package br.edu.ifpb.sahc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.sahc.dto.coordenacao.CoordenacaoResponse;
import br.edu.ifpb.sahc.model.Coordenacao;
import br.edu.ifpb.sahc.service.CoordenacaoService;
import br.edu.ifpb.sahc.service.UsuarioService;

@RestController
public class CoordenacaoController {

	@Autowired
	private CoordenacaoService coordenacaoService;
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping("/coordenacao")
	public List<Coordenacao> consultarCoordenacao() {
		return coordenacaoService.consultarCoordenacao();
	}
	
	@PostMapping("/coordenacao")
	public ResponseEntity<CoordenacaoResponse> cadastrarHorarioDisponivel(@RequestBody String horarios, @RequestHeader(value = "Authorization") String token) {
		return coordenacaoService.cadastrarHorarioDisponivel(horarios, usuarioService.adquireUsuario(token));
	}
	
	@DeleteMapping("/coordenacao")
	public ResponseEntity<CoordenacaoResponse> descadastrarHorarioDisponivel(@RequestBody String horarios, @RequestHeader(value = "Authorization") String token) {
		return coordenacaoService.descadastrarHorarioDisponivel(horarios, usuarioService.adquireUsuario(token));
	}
	
}
