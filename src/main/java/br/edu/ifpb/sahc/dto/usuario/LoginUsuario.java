package br.edu.ifpb.sahc.dto.usuario;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginUsuario {

	@NotBlank
	private String matricula;
	@NotBlank
	private String senha;
	@Override
	public String toString() {
		return "LoginUsuario [matricula=" + matricula + ", senha=" + senha + "]";
	}
	
	
}
