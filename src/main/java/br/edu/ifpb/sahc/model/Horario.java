package br.edu.ifpb.sahc.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@Entity(name = "horario")
public class Horario {

	@EmbeddedId
	private HorarioID horarioId;
	
	@MapsId("idCoordenacao")
	@JoinColumn(name = "idCoordenacao", referencedColumnName = "idCoordenacao")
	@ManyToOne
	private Coordenacao idCoordenacao;

	@Column(name = "horaInicial", insertable = false, updatable = false)
	private Timestamp horaInicial;
	
	@Column(name = "horaFinal", insertable = false, updatable = false)
	private Timestamp horaFinal;

	private boolean horarioAgendado;
	
	@OneToOne
	@JoinColumn(name = "matricula_usuario")
	private Usuario matriculaUsuario;
	
}
