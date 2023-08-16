package br.edu.ifpb.sahc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.ifpb.sahc.model.Usuario;
import br.edu.ifpb.sahc.repository.UsuarioRepository;

@Service
public class CriarAdmin{

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private Usuario usuario;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Value("${admin.matricula}")
	private String matriculaAdmin;
	@Value("${admin.nome}")
	private String nomeAdmin;
	@Value("${admin.senha}")
	private String senhaAdmin;
	@Value("${admin.email}")
	private String emailAdmin;


	public void run() {
		if(!usuarioRepository.existsByMatricula(matriculaAdmin)) {
			usuario.setMatricula(matriculaAdmin);
			usuario.setNome(nomeAdmin);
			usuario.setSenha(passwordEncoder.encode(senhaAdmin));
			usuario.setEmail(emailAdmin);
			usuario.setRole("admin");
			usuarioRepository.saveAndFlush(usuario);
		}
	}

}
