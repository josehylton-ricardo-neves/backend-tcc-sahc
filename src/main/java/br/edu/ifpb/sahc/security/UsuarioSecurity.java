package br.edu.ifpb.sahc.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.edu.ifpb.sahc.model.UsuarioModel;

//@Component
public class UsuarioSecurity implements UserDetails, Serializable{

	private static final long serialVersionUID = 1L;
	
	//private final Optional<User> usuario;
	
	private String username;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	
	
	/*public UserSecurity(Optional<User> usuario) {
        this.usuario = usuario;
    }*/
	
	
	public UsuarioSecurity(Optional<UsuarioModel> user) {
		this.username = user.get().getMatricula();
		this.password = user.get().getSenha();
		//user.get().getRegistry();
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		
		
		authorities = Arrays.asList(user.get().getRole()).stream().map(role -> {
			return new SimpleGrantedAuthority("ROLE_".concat(user.get().getRole()));
			//return new SimpleGrantedAuthority(user.get().getRole());
		}).collect(Collectors.toList());
		System.out.println("printou " + authorities);
		this.authorities = authorities;
		
		/*
		authorities = user.get().getRole().stream().map(role -> {
			//return new SimpleGrantedAuthority(role.getName().toString());
			return new SimpleGrantedAuthority("ROLE_".concat(role.getName()));
		}).collect(Collectors.toList());
		
		this.authorities = authorities;*/
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
		//return new ArrayList<>();
	}

	@Override
	public String getPassword() {
		return password;
		//return usuario.orElse(new User()).getPassword();
	}

	@Override
	public String getUsername() {
		return username;
		//return usuario.orElse(new User()).getRegistry();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
