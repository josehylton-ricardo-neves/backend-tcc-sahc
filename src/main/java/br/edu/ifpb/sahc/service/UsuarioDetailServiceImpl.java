package br.edu.ifpb.sahc.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.edu.ifpb.sahc.model.UsuarioModel;
import br.edu.ifpb.sahc.repository.UsuarioRepository;
import br.edu.ifpb.sahc.security.UsuarioSecurity;

@Service
public class UsuarioDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository userRepository;

	/*
	public UsuarioDetailServiceImpl() {
		
	}*/
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<UsuarioModel> usuario = userRepository.findByRegistry(username);
		
		if(usuario.isEmpty()) {
			throw new UsernameNotFoundException("Usuario " + username + " não encontrado.");
		}
		
		return new UsuarioSecurity(usuario);
	}

}
