package br.edu.ifpb.sahc.model;

import java.io.Serializable;

import javax.persistence.*;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@Entity(name = "usuario")
public class Usuario implements Serializable{

	private static final long serialVersionUID = 1L;

	
	@Id
	private String matricula;
	
	@Column(nullable = false)
	private String nome;

	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String senha;
	
	private String role;

	@ManyToOne
	@JoinColumn(name = "id_coordenacao")
	private Coordenacao idCoordenacao;
}
