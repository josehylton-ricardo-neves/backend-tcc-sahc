package br.edu.ifpb.sahc.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoordenacaoController {

	
	@PostMapping("/cadastrarHorarioDisponivel")
	public String cadastrarHorarioDisponivel(String usuario, String senha) {
		return "retorno Schedule";
	}
	
	@PostMapping("/descadastrarHorarioDisponivel")
	public String descadastrarHorarioDisponivel(String usuario, String senha) {
		return "retorno Schedule";
	}
	
}
