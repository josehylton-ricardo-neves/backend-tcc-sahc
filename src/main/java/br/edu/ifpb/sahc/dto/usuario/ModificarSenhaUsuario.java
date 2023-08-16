package br.edu.ifpb.sahc.dto.usuario;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ModificarSenhaUsuario {
	
	@NotBlank
	private String matricula;
	@NotBlank
	private String senhaAtual;
	@NotBlank
	private String novaSenha;

}
