package br.edu.ifpb.sahc.model;

import java.io.Serializable;
import java.util.UUID;
/*
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;*/
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@Entity
@Table(name = "usuario")
public class UsuarioModel implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@Column(nullable = false, unique = true)
	private String matricula;
	
	@Column(nullable = false)
	private String nome;

	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String senha;
	
	//@ManyToMany(targetEntity = Role.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
	//private List<Role> role;
	
	
	//@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	//@ManyToMany
	//@JoinTable(name="usuario_role", joinColumns=
	//{@JoinColumn(name="usuario_id")}, inverseJoinColumns=
	//{@JoinColumn(name="role_id")})
	private String role;
	//private List<Role> role;

}
