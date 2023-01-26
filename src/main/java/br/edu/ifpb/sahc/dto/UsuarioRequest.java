package br.edu.ifpb.sahc.dto;

import lombok.Data;

@Data
public class UsuarioRequest {

	private String matricula;
	private String nome;
	private String email;
	private String senha;
	private String role;
	private String novaSenha;
	
}
