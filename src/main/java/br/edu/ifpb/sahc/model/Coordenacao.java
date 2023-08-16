package br.edu.ifpb.sahc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@Entity(name = "coordenacao")
public class Coordenacao {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idCoordenacao;
	
	@Column(nullable = false, unique = true)
	private String emailCoordenacao;
	
	@Column(nullable = false, unique = true)
	private String nomeCoordenacao;
	
	@Column(nullable = true)
	private long matriculaCoordenador;
	
	@Column(nullable = true)
	private String nomeCoordenador;

}
