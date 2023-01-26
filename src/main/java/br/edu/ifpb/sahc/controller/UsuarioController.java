package br.edu.ifpb.sahc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.sahc.dto.UsuarioRequest;
import br.edu.ifpb.sahc.dto.UsuarioResponse;
import br.edu.ifpb.sahc.model.UsuarioModel;
import br.edu.ifpb.sahc.service.UsuarioService;

@RestController
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;

	
	@PostMapping("/")
	public String Login(@RequestBody UsuarioModel usuario) {
		System.out.println(usuario.toString());
		return "retorno Schedule";
	}
	
	@PostMapping("/cadastrarUsuario")
	public ResponseEntity<UsuarioResponse> registerUser(@RequestBody UsuarioModel usuario) {
		return usuarioService.registrarUsuario(usuario.getMatricula(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getRole());
	}
	
	@PatchMapping("/modificarSenha")
	public ResponseEntity<UsuarioResponse> modificarSenha(@RequestBody UsuarioRequest usuario, @RequestHeader(value = "Authorization") String header) {
		return usuarioService.modificarSenha(usuario.getMatricula(), usuario.getSenha(), usuario.getNovaSenha(), header);
	}
	
	@Secured("ROLE_ADMIN")
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/recuperarSenha")
	public ResponseEntity<UsuarioResponse> recuperarSenha(@RequestBody UsuarioRequest usuario) {
		return usuarioService.recuperarSenha(usuario.getMatricula(), usuario.getEmail());
	}
}
