package br.edu.ifpb.sahc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import br.edu.ifpb.sahc.dto.admin.AdminResponse;
import br.edu.ifpb.sahc.dto.admin.CoordenacaoDTO;
import br.edu.ifpb.sahc.dto.admin.CrudUsuario;
import br.edu.ifpb.sahc.dto.usuario.CadastrarUsuario;
import br.edu.ifpb.sahc.dto.usuario.UsuarioList;
import br.edu.ifpb.sahc.dto.usuario.UsuarioResponse;
import br.edu.ifpb.sahc.model.Coordenacao;
import br.edu.ifpb.sahc.model.Usuario;
import br.edu.ifpb.sahc.repository.CoordenacaoRepository;
import br.edu.ifpb.sahc.repository.UsuarioRepository;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioServiceTest {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private CoordenacaoRepository coordenacaoRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private UsuarioService usuarioService;
	
	private Coordenacao coordenacao;
	
	
	@BeforeAll
	public void setUp() {
		CoordenacaoDTO crudCoordenacao = new CoordenacaoDTO();
        crudCoordenacao.setNomeCoordenacao("Engenharia");
        crudCoordenacao.setEmailCoordenacao("coord_eng@hotmail.com");

        adminService.cadastrarCoordenacao(crudCoordenacao);

        coordenacao = coordenacaoRepository.findByEmailCoordenacao(crudCoordenacao.getEmailCoordenacao());
        
	  }
	
	@Test
	public void testCadastrarUsuarioSucesso() {
		
		CadastrarUsuario usuario = new CadastrarUsuario();
		usuario.setMatricula("123456123456");
		usuario.setNome("Jose_teste");
		usuario.setEmail("jose_teste@hotmail.com");
		usuario.setSenha("Jose12345@");
		usuario.setIdCoordenacao(coordenacao.getIdCoordenacao());
		
		ResponseEntity<UsuarioResponse> response1 = usuarioService.cadastrarUsuario(usuario);
		
	    assertEquals(HttpStatus.CREATED, response1.getStatusCode());

	  }
	
	@Test
	public void testCadastrarUsuarioErroSenha() {
		
		CadastrarUsuario usuario = new CadastrarUsuario();
		usuario.setMatricula("123456");
		usuario.setNome("Jose");
		usuario.setEmail("jose@hotmail.com");
		usuario.setSenha("jose12345@");
		usuario.setIdCoordenacao(coordenacao.getIdCoordenacao());
		
		
        ResponseEntity<UsuarioResponse> response1 = usuarioService.cadastrarUsuario(usuario);
        
        usuario.setSenha("josesenhaerro@");
		ResponseEntity<UsuarioResponse> response2 = usuarioService.cadastrarUsuario(usuario);
        
		usuario.setSenha("JOSE12345@");
		ResponseEntity<UsuarioResponse> response3 = usuarioService.cadastrarUsuario(usuario);
        
		usuario.setSenha("12345678@");
		ResponseEntity<UsuarioResponse> response4 = usuarioService.cadastrarUsuario(usuario);
        
		usuario.setSenha("Jose12345678");
		ResponseEntity<UsuarioResponse> response5 = usuarioService.cadastrarUsuario(usuario);
        
		usuario.setSenha("jose");
		ResponseEntity<UsuarioResponse> response6 = usuarioService.cadastrarUsuario(usuario);
        
		usuario.setSenha("Josemaiorque20123456779@");
		ResponseEntity<UsuarioResponse> response7 = usuarioService.cadastrarUsuario(usuario);
		
		
	    assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
	    assertEquals("Insira um password valido", response1.getBody().getMensagem());
	    
	    assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
	    assertEquals("Insira um password valido", response2.getBody().getMensagem());
	    
	    assertEquals(HttpStatus.BAD_REQUEST, response3.getStatusCode());
	    assertEquals("Insira um password valido", response3.getBody().getMensagem());
	    
	    assertEquals(HttpStatus.BAD_REQUEST, response4.getStatusCode());
	    assertEquals("Insira um password valido", response4.getBody().getMensagem());
	    
	    assertEquals(HttpStatus.BAD_REQUEST, response5.getStatusCode());
	    assertEquals("Insira um password valido", response5.getBody().getMensagem());
	    
	    assertEquals(HttpStatus.BAD_REQUEST, response6.getStatusCode());
	    assertEquals("Insira um password valido", response6.getBody().getMensagem());
	    
	    assertEquals(HttpStatus.BAD_REQUEST, response7.getStatusCode());
	    assertEquals("Insira um password valido", response7.getBody().getMensagem());
	  }

	@Test
	public void testCadastrarUsuarioCamposInvalidos() {
		
		CadastrarUsuario usuario = new CadastrarUsuario();
		usuario.setMatricula("123123123");
		usuario.setNome(null);
		usuario.setEmail("jose_teste2@hotmail.com");
		usuario.setSenha("Jose12345@");
		usuario.setIdCoordenacao(coordenacao.getIdCoordenacao());
		
		CadastrarUsuario usuario2 = new CadastrarUsuario();
		usuario2.setMatricula("123123123123");
		usuario2.setNome("JOSE_teste");
		usuario2.setEmail(null);
		usuario2.setSenha("Jose12345@");
		usuario2.setIdCoordenacao(coordenacao.getIdCoordenacao());
		
		ResponseEntity<UsuarioResponse> response1 = usuarioService.cadastrarUsuario(usuario);
		ResponseEntity<UsuarioResponse> response2 = usuarioService.cadastrarUsuario(usuario2);

	    assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
	    assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
	    
	  }
	
	@Test
	public void testBuscarUsuario() {
		
		ResponseEntity<List<UsuarioList>> response = usuarioService.buscarUsuario();
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    
	  }
	
	@Test
	public void testFindByMatricula() {
		
		CadastrarUsuario usuario = new CadastrarUsuario();
		usuario.setMatricula("12341234");
		usuario.setNome("aluno teste");
		usuario.setEmail("aluno_teste@hotmail.com");
		usuario.setSenha("Aluno12345@");
		usuario.setIdCoordenacao(coordenacao.getIdCoordenacao());
		
		ResponseEntity<UsuarioResponse> response = usuarioService.cadastrarUsuario(usuario);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		Optional<Usuario> response1 = usuarioService.findByMatricula("12341234");

	    assertNotNull(response1);
	    
	  }
	
	@Test
	public void testDelete() {
		
		CadastrarUsuario usuario = new CadastrarUsuario();
		usuario.setMatricula("1234512345");
		usuario.setNome("aluno teste 2");
		usuario.setEmail("aluno_teste2@hotmail.com");
		usuario.setSenha("Aluno12345@");
		usuario.setIdCoordenacao(coordenacao.getIdCoordenacao());
		
		ResponseEntity<UsuarioResponse> response = usuarioService.cadastrarUsuario(usuario);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		Usuario usuarioModel = new Usuario();
		BeanUtils.copyProperties(usuario, usuarioModel);
		
		usuarioService.delete(usuarioModel);

	    assertEquals(Optional.empty(), usuarioService.findByMatricula("1234512345"));
	    
	  }
	
	@Test
	public void testDeleteAllByIdCoordenacao() {
		
		usuarioService.deleteAllByIdCoordenacao(coordenacao);
	    assertEquals(1, usuarioRepository.findAll().size());
	    
	  }
	
	
	@Test
    public void testAlterarPrivilegios() {
    	CoordenacaoDTO crudCoordenacao = new CoordenacaoDTO();
        crudCoordenacao.setNomeCoordenacao("Eng Comput 41");
        crudCoordenacao.setEmailCoordenacao("coordenacao_teste_41@hotmail.com");
        crudCoordenacao.setMatriculaCoordenador(12345678);
        crudCoordenacao.setNomeCoordenador("Coordenador DA");

        ResponseEntity<AdminResponse> response = adminService.cadastrarCoordenacao(crudCoordenacao);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        CadastrarUsuario usuario = new CadastrarUsuario();
        usuario.setMatricula("12312123");
        usuario.setEmail("usuario_teste@hotmail.com");
        usuario.setIdCoordenacao(coordenacao.getIdCoordenacao());
        usuario.setNome("francisco");
        usuario.setSenha("Francisco123*");
        
        ResponseEntity<UsuarioResponse> response1 = usuarioService.cadastrarUsuario(usuario);
		
	    assertEquals(HttpStatus.CREATED, response1.getStatusCode());
	    
	    assertEquals("aluno", usuarioRepository.findByMatricula("12312123").get().getRole());
	    
	    CrudUsuario crudUsuario = new CrudUsuario();
	    crudUsuario.setMatricula(usuario.getMatricula());
	    crudUsuario.setRole("coord");
	    usuarioService.alterarPrivilegioUsuario(crudUsuario);
	    assertEquals("coord", usuarioRepository.findByMatricula("12312123").get().getRole());
    }
	
	
	
	@Test
    public void testModificarSenha() {
        CadastrarUsuario usuario = new CadastrarUsuario();
        usuario.setMatricula("121212");
        usuario.setEmail("usuario_teste_modifica@hotmail.com");
        usuario.setIdCoordenacao(coordenacao.getIdCoordenacao());
        usuario.setNome("josefa");
        usuario.setSenha("Francisco123*");
        
        ResponseEntity<UsuarioResponse> response = usuarioService.cadastrarUsuario(usuario);
	    assertEquals(HttpStatus.CREATED, response.getStatusCode());
	    
	    
	    ResponseEntity<UsuarioResponse> response1 = usuarioService.modificarSenha("121212", "Francisco123*", "Francisco123*", "12121");
	    assertEquals("Insira o usuario correto", response1.getBody().getMensagem());
	    
	    ResponseEntity<UsuarioResponse> response2 = usuarioService.modificarSenha("121212", "Francisco123*", "francisco12*", "121212");
	    assertEquals("Insira uma nova senha valida", response2.getBody().getMensagem());
	    
	    ResponseEntity<UsuarioResponse> response3 = usuarioService.modificarSenha("1212122", "Francisco123*", "Francisco12*", "1212122");
	    assertEquals("Usuario invalido", response3.getBody().getMensagem());
	    
	    ResponseEntity<UsuarioResponse> response4 = usuarioService.modificarSenha("121212", "Francisco12*", "Francisco12123*", "121212");
	    assertEquals("Senha incorreta, por favor insira a senha correta", response4.getBody().getMensagem());
	    
	    ResponseEntity<UsuarioResponse> response5 = usuarioService.modificarSenha("121212", "Francisco123*", "Francisco12123*", "121212");
	    assertEquals(HttpStatus.OK, response5.getStatusCode());

    }
	
	
	@Test
    public void testRecuperarSenha() {

        CadastrarUsuario usuario = new CadastrarUsuario();
        usuario.setMatricula("123451234512");
        usuario.setEmail("usuario_teste_recupera@hotmail.com");
        usuario.setIdCoordenacao(coordenacao.getIdCoordenacao());
        usuario.setNome("josefa_teste");
        usuario.setSenha("Josefa123*");
        
        ResponseEntity<UsuarioResponse> response = usuarioService.cadastrarUsuario(usuario);
	    assertEquals(HttpStatus.CREATED, response.getStatusCode());
	    
	    ResponseEntity<UsuarioResponse> response1 = usuarioService.recuperarSenha("", "");
	    assertEquals("Usuario não encontrado", response1.getBody().getMensagem());
	    
	    ResponseEntity<UsuarioResponse> response2 = usuarioService.recuperarSenha("123451234512", "");
	    assertEquals(HttpStatus.OK, response2.getStatusCode());
	    
	    ResponseEntity<UsuarioResponse> response3 = usuarioService.recuperarSenha("", "usuario_teste_recupera@hotmail.com");
	    assertEquals(HttpStatus.OK, response3.getStatusCode());
    }

}
