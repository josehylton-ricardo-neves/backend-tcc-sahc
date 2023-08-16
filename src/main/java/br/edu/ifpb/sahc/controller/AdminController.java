package br.edu.ifpb.sahc.controller;

import javax.validation.Valid;

import br.edu.ifpb.sahc.dto.admin.CoordenacaoDTO;
import br.edu.ifpb.sahc.dto.usuario.UsuarioList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.sahc.dto.admin.AdminResponse;
import br.edu.ifpb.sahc.dto.admin.CrudCoordenacao;
import br.edu.ifpb.sahc.dto.admin.CrudUsuario;
import br.edu.ifpb.sahc.service.AdminService;

@RestController
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@PostMapping("/cadastrarCoordenacao")
	public ResponseEntity<AdminResponse> cadastrarCoordenacao(@Valid @RequestBody CoordenacaoDTO coordenacao) {
		return adminService.cadastrarCoordenacao(coordenacao);
	}
	
	@PatchMapping("/atualizarCoordenacao")
	public ResponseEntity<AdminResponse> atualizarCoordenacao(@Valid @RequestBody CrudCoordenacao coordenacao) {
		return adminService.atualizarCoordenacao(coordenacao);
	}
	
	@DeleteMapping("/descadastrarCoordenacao")
	public ResponseEntity<AdminResponse> descadastrarCoordenacao(@Valid @RequestBody CrudCoordenacao coordenacao) {
		return adminService.descadastrarCoordenacao(coordenacao);
	}
	
	@PatchMapping("/alterarPrivilegiosUsuario")
	public ResponseEntity<AdminResponse> alterarPrivilegiosUsuario(@RequestBody CrudUsuario usuario) {
		return adminService.alterarPrivilegiosUsuario(usuario);
	}

	@DeleteMapping("/deletarUsuario")
	public ResponseEntity<AdminResponse> deletarUsuario(@Valid @RequestBody UsuarioList usuario) {
		return adminService.deletarUsuario(usuario);
	}

}
