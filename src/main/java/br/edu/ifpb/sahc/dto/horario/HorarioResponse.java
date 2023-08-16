package br.edu.ifpb.sahc.dto.horario;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({"timestamp", "status", "mensagem"})
public class HorarioResponse {

	private Date timestamp;
	private int status;
	private String mensagem;
	
}
