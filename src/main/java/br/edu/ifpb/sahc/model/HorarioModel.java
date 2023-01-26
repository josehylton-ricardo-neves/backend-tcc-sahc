package br.edu.ifpb.sahc.model;

import java.util.UUID;

/*
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;*/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "horario")
public class HorarioModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private long idCoordenacao;
	private String nomeCoordenacao;
	private String horarioDisponivel;
	private boolean horarioAgendado;
	private long matriculaDiscente;
	
}
