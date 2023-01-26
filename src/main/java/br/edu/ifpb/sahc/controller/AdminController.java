package br.edu.ifpb.sahc.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.sahc.model.CoordenacaoModel;

@RestController
public class AdminController {
	
	
	@PostMapping("/cadastrarCoordenacao")
	public String cadastrarCoordenacao(@RequestBody CoordenacaoModel coordenacao) {
		return "retorno Schedule";
	}
	
	@PostMapping("/alterarPrivilegiosUsuario")
	public String alterarPrivilegiosUsuario(String usuario, String senha) {
		return "retorno Schedule";
	}

}
