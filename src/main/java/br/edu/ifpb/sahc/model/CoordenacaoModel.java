package br.edu.ifpb.sahc.model;
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
@Entity(name = "coordenacao")
public class CoordenacaoModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private long matriculaCoordenador;
	
	@Column(nullable = false)
	private String nomeCoordenador;
	
	@Column(nullable = false)
	private String emailCoordenacao;
	
	@Column(nullable = false)
	private String nomeCoordenacao;

}
