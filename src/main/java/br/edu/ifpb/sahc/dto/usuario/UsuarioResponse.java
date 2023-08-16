package br.edu.ifpb.sahc.dto.usuario;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Component
@Data
@JsonPropertyOrder({"timestamp", "status", "mensagem"})
public class UsuarioResponse {

	private Date timestamp;
	private int status;
	private String mensagem;
	
}
