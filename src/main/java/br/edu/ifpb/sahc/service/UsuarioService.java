package br.edu.ifpb.sahc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import br.edu.ifpb.sahc.dto.admin.CrudUsuario;
import br.edu.ifpb.sahc.dto.usuario.CadastrarUsuario;
import br.edu.ifpb.sahc.dto.usuario.UsuarioList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.edu.ifpb.sahc.dto.usuario.UsuarioResponse;
import br.edu.ifpb.sahc.model.Coordenacao;
import br.edu.ifpb.sahc.model.Usuario;
import br.edu.ifpb.sahc.repository.UsuarioRepository;
import br.edu.ifpb.sahc.security.JWTAutenticacaoFilter;
import br.edu.ifpb.sahc.security.JWTValidacaoFilter;
import net.bytebuddy.utility.RandomString;

@Service
public class UsuarioService {

	public final static String PATTERNPASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=*!])(?=\\S+$).{8,20}$";

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private EmailService emailService;
	@Autowired
	private Usuario usuario;
	@Autowired
	private CoordenacaoService coordenacaoService;

	public ResponseEntity<List<UsuarioList>> buscarUsuario() {

		List<UsuarioList> usuarioLists = new ArrayList<>();

		List<Usuario> usuarios = usuarioRepository.findAll();

		if(!usuarios.isEmpty()){
			ModelMapper modelMapper = new ModelMapper();
			usuarioLists = usuarios.stream()
					.map(usuario -> modelMapper.map(usuario,UsuarioList.class)).collect(Collectors.toList());
			return ResponseEntity.ok(usuarioLists);
		}

		return ResponseEntity.ok(usuarioLists);


    }
	public ResponseEntity<UsuarioResponse> cadastrarUsuario(CadastrarUsuario cadastroUsuario) {

		if(!checkPassword(cadastroUsuario.getSenha())) {
			return responseEntity(400, "Insira um password valido", HttpStatus.BAD_REQUEST);
		}
		
		try {
			usuario.setMatricula(cadastroUsuario.getMatricula());
			usuario.setNome(cadastroUsuario.getNome());
			usuario.setEmail(cadastroUsuario.getEmail());
			usuario.setSenha(passwordEncoder.encode(cadastroUsuario.getSenha()));
			usuario.setIdCoordenacao(coordenacaoService.getById(cadastroUsuario.getIdCoordenacao()));
			usuario.setRole("aluno");
	
			usuarioRepository.save(usuario);
			emailService.enviarEmail(usuario.getEmail(), "Cadastro no SAHC", "Cadastro Realizado com Sucesso");
			
			return responseEntity(null, null, HttpStatus.CREATED);
		}catch(Exception e){
			return responseEntity(400, null, HttpStatus.BAD_REQUEST);
		}
	}


	public ResponseEntity<UsuarioResponse> modificarSenha(String matricula, String senha, String novaSenha, String matriculaToken) {

		if (!matriculaToken.equals(matricula)) {
			return responseEntity(400, "Insira o usuario correto", HttpStatus.BAD_REQUEST);
		}

		if (!checkPassword(novaSenha)) {
			return responseEntity(400, "Insira uma nova senha valida", HttpStatus.BAD_REQUEST);
		}

		Optional<Usuario> usuario = usuarioRepository.findByMatricula(matricula);

		if(usuario.isEmpty()) {
			return responseEntity(400, "Usuario invalido", HttpStatus.BAD_REQUEST);
		}

		int update = 0;

		if(passwordEncoder.matches(senha, usuario.get().getSenha())) {
			update = usuarioRepository.updateSenha(matricula, passwordEncoder.encode(novaSenha));
		}else {
			return responseEntity(400, "Senha incorreta, por favor insira a senha correta", HttpStatus.BAD_REQUEST);
		}

		if(update == 1) {
			return responseEntity(null, null, HttpStatus.OK);
		}else {
			return responseEntity(null, null, HttpStatus.BAD_REQUEST);
		}

	}


	public String adquireUsuario(String token) {
		token = token.replace(JWTValidacaoFilter.PREFIX_ATTRIBUTE, "");//.trim();
		return JWT.require(Algorithm.HMAC512(JWTAutenticacaoFilter.TOKEN_CODE)).build().verify(token).getSubject();
	}


	public ResponseEntity<UsuarioResponse> recuperarSenha(String matricula, String email) {

		Optional<Usuario> usuario = null;
		if(!email.isBlank()){
			usuario = usuarioRepository.findByEmail(email);
		} else if (!matricula.isBlank()) {
			usuario = usuarioRepository.findByMatricula(matricula);
		}


		if(usuario == null || !usuario.isPresent()) {
			return responseEntity(400, "Usuario não encontrado", HttpStatus.BAD_REQUEST);
		}
		RandomString senhaAleatoria = new RandomString(8);
		String novaSenha = senhaAleatoria.nextString();
		
		usuarioRepository.updateSenha(usuario.get().getMatricula(),passwordEncoder.encode(novaSenha));
		emailService.enviarEmail(usuario.get().getEmail(), "Recuperação de senha SAHC", "Sua nova senha é: " + novaSenha);

		return responseEntity(200, "Uma nova senha foi enviada para " + usuario.get().getEmail(), HttpStatus.OK);
	}
	
	
	public int alterarPrivilegioUsuario(CrudUsuario usuario) {
		return usuarioRepository.alterarPrivilegioUsuario(usuario.getMatricula(), usuario.getRole());
	}
	
	public Optional<Usuario> findByMatricula(String matricula) {
		return usuarioRepository.findByMatricula(matricula);
	}
	
	public void delete(Usuario usuario) {
		usuarioRepository.delete(usuario);
	}
	
	public void deleteAllByIdCoordenacao(Coordenacao coordenacao) {
		usuarioRepository.deleteAllByIdCoordenacao(coordenacao);
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
