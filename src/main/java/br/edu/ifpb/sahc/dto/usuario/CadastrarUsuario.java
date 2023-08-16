package br.edu.ifpb.sahc.dto.usuario;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CadastrarUsuario {

	@NotBlank
	private String matricula;
	@NotBlank
	private String nome;
	@NotBlank
	@Email
	private String email;
	@NotBlank
	private String senha;
	@NotNull
	@JsonProperty("id_coordenacao")
	private Long idCoordenacao;
}
