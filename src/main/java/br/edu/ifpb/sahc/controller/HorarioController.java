package br.edu.ifpb.sahc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HorarioController {

	
	@GetMapping("/solicitarHorarioDisponivel")
	public String solicitarHorarioDisponivel() {
		return "retorno Schedule";
	}
	
	@PostMapping("/agendarHorarioDisponivel")
	public String agendarHorarioDisponivel(String usuario, String senha) {
		return "retorno Schedule";
	}

	@PostMapping("/desagendarHorarioDisponivel")
	public String desagendarHorarioDisponivel(String usuario, String senha) {
		return "retorno Schedule";
	}
}
