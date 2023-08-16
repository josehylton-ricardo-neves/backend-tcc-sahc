package br.edu.ifpb.sahc.controller;

import javax.validation.Valid;

import br.edu.ifpb.sahc.dto.usuario.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ifpb.sahc.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@RestController
@Api( tags = "usuario-controller")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;

	@ApiOperation(value = "Metodo usado para realizar o login no sistema")
	@PostMapping("/login")
	public void Login(@Valid @RequestBody LoginUsuario usuario) {
	}
	
	@ApiOperation(value = "Metodo usado para cadastrar um usuario no sistema")
	@PostMapping("/cadastrarUsuario")
	public ResponseEntity<UsuarioResponse> cadastrarUsuario(@Valid @RequestBody CadastrarUsuario usuario) {
		return usuarioService.cadastrarUsuario(usuario);
	}
	@ApiOperation(value = "Metodo usado para buscar todos os usuários cadastrados")
	@GetMapping("/buscarUsuarios")
	public ResponseEntity<List<UsuarioList>> buscasUsuarios(@RequestHeader (value = "Authorization" ) String token) {
		return usuarioService.buscarUsuario();
	}
	
	@ApiOperation(value = "Metodo usado para modificar a senha do usuario")
	@PatchMapping("/modificarSenha")
	public ResponseEntity<UsuarioResponse> modificarSenha(@Valid @RequestBody ModificarSenhaUsuario usuario, @RequestHeader(value = "Authorization") String token) {
		return usuarioService.modificarSenha(usuario.getMatricula(), usuario.getSenhaAtual(), usuario.getNovaSenha(), usuarioService.adquireUsuario(token));
	}

	@ApiOperation(value = "Metodo usado para recuperar a senha do usuario")
	@PostMapping("/recuperarSenha")
	public ResponseEntity<UsuarioResponse> recuperarSenha(@Valid @RequestBody RecuperarSenhaUsuario usuario) {
		return usuarioService.recuperarSenha(usuario.getMatricula(), usuario.getEmail());
	}

}
