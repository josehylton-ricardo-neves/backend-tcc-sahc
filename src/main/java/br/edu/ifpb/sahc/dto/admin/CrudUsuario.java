package br.edu.ifpb.sahc.dto.admin;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CrudUsuario {

	@NotBlank
	private String matricula;
	@NotBlank
	private String role;
}
