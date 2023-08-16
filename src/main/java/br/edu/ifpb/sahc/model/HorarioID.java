package br.edu.ifpb.sahc.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HorarioID implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long idCoordenacao;
	private Timestamp horaInicial;
	private Timestamp horaFinal;
}
