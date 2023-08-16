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

import br.edu.ifpb.sahc.model.Usuario;

public class UsuarioSecurity implements UserDetails, Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private String username;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	
	
	public UsuarioSecurity(Optional<Usuario> user) {
		this.username = user.get().getMatricula();
		this.password = user.get().getSenha();
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		authorities = Arrays.asList(user.get().getRole()).stream().map(role -> {
			return new SimpleGrantedAuthority("ROLE_".concat(user.get().getRole()));
		}).collect(Collectors.toList());
		this.authorities = authorities;
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
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
