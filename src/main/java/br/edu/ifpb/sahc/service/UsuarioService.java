package br.edu.ifpb.sahc.service;

import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.edu.ifpb.sahc.dto.UsuarioResponse;
import br.edu.ifpb.sahc.model.UsuarioModel;
import br.edu.ifpb.sahc.repository.UsuarioRepository;
import br.edu.ifpb.sahc.security.JWTAutenticacaoFilter;
import br.edu.ifpb.sahc.security.JWTValidacaoFilter;
import net.bytebuddy.utility.RandomString;

@Service
public class UsuarioService {
	
	public final static String PATTERNPASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private EmailService emailService;
	@Autowired
	private UsuarioModel usuario;

	

	public ResponseEntity<UsuarioResponse> registrarUsuario(String matricula, String nome, String email, String senha, String role) {
		
		//usar chain of responsability
		
		if(matricula.isBlank()) {
			return responseEntity(400, "Insira um registro valido", HttpStatus.BAD_REQUEST);
		}
		
		if(nome.isBlank()) {
			return responseEntity(400, "Insira um nome valido", HttpStatus.BAD_REQUEST);
		}
		
		if(email.isBlank()) {
			return responseEntity(400, "Insira um email valido", HttpStatus.BAD_REQUEST);
		}
		
		if(senha.isBlank() || !checkPassword(senha)) {
			return responseEntity(400, "Insira um password valido", HttpStatus.BAD_REQUEST);
		}
		
		usuario.setMatricula(matricula);
		usuario.setNome(nome);
		usuario.setEmail(email);
		usuario.setSenha(passwordEncoder.encode(senha));
		usuario.setRole("USER");
		
		usuarioRepository.save(usuario);
		emailService.enviarEmail(usuario.getEmail(), "Cadastro no SAHC", "Cadastro Realizado com Sucesso");
		return responseEntity(null, null, HttpStatus.CREATED);
	}
	
	
	
	
	
	public Optional<UsuarioModel> consultarUsuario(String matricula) {
		return usuarioRepository.findByRegistry(matricula);
	}
	public void recuperarSenha() {
		
	}
	
	
	
	
	
	public ResponseEntity<UsuarioResponse> modificarSenha(String matricula, String senha, String novaSenha, String token) {
		
		if(!token.startsWith(JWTValidacaoFilter.PREFIX_ATTRIBUTE)) {
			return responseEntity(400, "Bad Request", HttpStatus.BAD_REQUEST);
		}
		
		token = token.replace(JWTValidacaoFilter.PREFIX_ATTRIBUTE, "");//.trim();
		String userToken = JWT.require(Algorithm.HMAC512(JWTAutenticacaoFilter.TOKEN_CODE)).build().verify(token).getSubject();
		
		if (!userToken.equals(matricula)) {
			return responseEntity(400, "Insira o usuario correto", HttpStatus.BAD_REQUEST);
		}
		
		if (!checkPassword(novaSenha)) {
			return responseEntity(400, "Insira um password valido", HttpStatus.BAD_REQUEST);
		}
		
		Optional<UsuarioModel> usuario = usuarioRepository.findByRegistry(matricula);
		
		if(usuario == null) {
			return responseEntity(400, "Usuario invalido", HttpStatus.BAD_REQUEST);
		}
		
		int update = 0;
		
		if(passwordEncoder.matches(senha, usuario.get().getSenha())) {
			update = usuarioRepository.updatePassword(matricula, passwordEncoder.encode(novaSenha));
		}else {
			return responseEntity(400, "Senha incorreta, por favor insira a senha correta", HttpStatus.BAD_REQUEST);
		}
		
		if(update == 1) {
			return responseEntity(null, null, HttpStatus.OK);
		}else {
			return responseEntity(null, null, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	public ResponseEntity<UsuarioResponse> recuperarSenha(String matricula, String email) {
		
		Optional<UsuarioModel> usuario = usuarioRepository.findByRegistry(matricula);
		
		if(usuario == null ) {
			return responseEntity(400, "Usuario não encontrado", HttpStatus.BAD_REQUEST);
		}
		RandomString senhaAleatoria = new RandomString(20);
		String novaSenha = senhaAleatoria.nextString();
		usuarioRepository.updatePassword(matricula, passwordEncoder.encode(novaSenha));
		emailService.enviarEmail(usuario.get().getEmail(), "Recuperação de senha SAHC", "Sua nova senha é: " + novaSenha);
		
		return responseEntity(200, "Senha modificada com sucesso, acesse seu email", HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	private ResponseEntity<UsuarioResponse> responseEntity(Integer status, String mensagem, HttpStatus httpStatus) {
		
		if(status != null && mensagem != null) {
			UsuarioResponse usuarioResponse = new UsuarioResponse();
			usuarioResponse.setTimestamp(new Date());
			usuarioResponse.setStatus(status);
			usuarioResponse.setMensagem(mensagem);
			return new ResponseEntity<UsuarioResponse>(usuarioResponse, httpStatus);
		}
		return new ResponseEntity<UsuarioResponse>(httpStatus);
	}
	
	private boolean checkPassword(String senha) {
		if(senha.length() < 8 || senha.length() > 20) {
			return false;
		}
		Pattern pattern = Pattern.compile(PATTERNPASSWORD);
		Matcher passwordMatcher = pattern.matcher(senha);
		return passwordMatcher.matches();
	}
	
}
