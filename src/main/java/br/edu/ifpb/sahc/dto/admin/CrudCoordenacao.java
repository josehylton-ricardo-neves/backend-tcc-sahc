package br.edu.ifpb.sahc.dto.admin;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CrudCoordenacao {

	@NotNull
	private Long idCoordenacao;
	@NotBlank
	@Email
	private String emailCoordenacao;
	@NotBlank
	private String nomeCoordenacao;
	private long matriculaCoordenador;
	private String nomeCoordenador;
}
