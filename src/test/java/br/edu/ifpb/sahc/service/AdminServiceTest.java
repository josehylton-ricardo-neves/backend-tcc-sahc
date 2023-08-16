package br.edu.ifpb.sahc.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.edu.ifpb.sahc.dto.admin.AdminResponse;
//import br.edu.ifpb.sahc.dto.admin.CadastrarCoodenacaoDTO;
import br.edu.ifpb.sahc.dto.admin.CoordenacaoDTO;
import br.edu.ifpb.sahc.dto.admin.CrudCoordenacao;
import br.edu.ifpb.sahc.dto.admin.CrudUsuario;
import br.edu.ifpb.sahc.dto.usuario.CadastrarUsuario;
import br.edu.ifpb.sahc.dto.usuario.UsuarioList;
import br.edu.ifpb.sahc.dto.usuario.UsuarioResponse;
import br.edu.ifpb.sahc.model.Coordenacao;
import br.edu.ifpb.sahc.repository.CoordenacaoRepository;
import br.edu.ifpb.sahc.repository.UsuarioRepository;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminServiceTest {
	
	@Autowired
    private AdminService adminService;
    @Autowired
    private CoordenacaoRepository coordenacaoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioService usuarioService;

    
    @Test
    public void testCadastrarCoordenacao() {
    	CoordenacaoDTO crudCoordenacao = new CoordenacaoDTO();
        crudCoordenacao.setNomeCoordenacao("Eng Comput 1");
        crudCoordenacao.setEmailCoordenacao("coordenacao_teste_1@hotmail.com");
        crudCoordenacao.setMatriculaCoordenador(123456);
        crudCoordenacao.setNomeCoordenador("Coordenador A");

        ResponseEntity<AdminResponse> response = adminService.cadastrarCoordenacao(crudCoordenacao);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Coordenacao coordenacao = coordenacaoRepository.findByEmailCoordenacao(crudCoordenacao.getEmailCoordenacao());
        assertEquals(crudCoordenacao.getNomeCoordenacao(), coordenacao.getNomeCoordenacao());
        assertEquals(crudCoordenacao.getEmailCoordenacao(), coordenacao.getEmailCoordenacao());
        assertEquals(crudCoordenacao.getMatriculaCoordenador(), coordenacao.getMatriculaCoordenador());
    }

    @Test
    public void testAtualizarCoordenacao() {
    	CoordenacaoDTO crudCoordenacao = new CoordenacaoDTO();
        crudCoordenacao.setNomeCoordenacao("Eng Comput 2");
        crudCoordenacao.setEmailCoordenacao("coordenacao_teste_2@hotmail.com");
        crudCoordenacao.setMatriculaCoordenador(1234567);
        crudCoordenacao.setNomeCoordenador("Coordenador B");

        ResponseEntity<AdminResponse> response = adminService.cadastrarCoordenacao(crudCoordenacao);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        CrudCoordenacao coordenacaoDTO = new CrudCoordenacao();
        BeanUtils.copyProperties(coordenacaoRepository.findByEmailCoordenacao(crudCoordenacao.getEmailCoordenacao()), coordenacaoDTO);
        coordenacaoDTO.setNomeCoordenacao("Eng Comput 2 novo");
        coordenacaoDTO.setEmailCoordenacao("coordenacao_teste_novo@hotmail.com");
        coordenacaoDTO.setMatriculaCoordenador(1234567890);
        coordenacaoDTO.setNomeCoordenador("Coordenador B novo");
        
        response = adminService.atualizarCoordenacao(coordenacaoDTO);
        
        Coordenacao coordenacao = coordenacaoRepository.findByEmailCoordenacao(coordenacaoDTO.getEmailCoordenacao());
        
        assertEquals(coordenacaoDTO.getNomeCoordenacao(), coordenacao.getNomeCoordenacao());
        assertEquals(coordenacaoDTO.getEmailCoordenacao(), coordenacao.getEmailCoordenacao());
        assertEquals(coordenacaoDTO.getMatriculaCoordenador(), coordenacao.getMatriculaCoordenador());
        assertEquals(coordenacaoDTO.getNomeCoordenador(), coordenacao.getNomeCoordenador());

    }
    
    @Test
    public void testDescadastrarCoordenacao() {
    	CoordenacaoDTO crudCoordenacao = new CoordenacaoDTO();
        crudCoordenacao.setNomeCoordenacao("Eng Comput 3");
        crudCoordenacao.setEmailCoordenacao("coordenacao_teste_3@hotmail.com");
        crudCoordenacao.setMatriculaCoordenador(1234567899);
        crudCoordenacao.setNomeCoordenador("Coordenador C");


        ResponseEntity<AdminResponse> response = adminService.cadastrarCoordenacao(crudCoordenacao);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Coordenacao coordenacao = coordenacaoRepository.findByEmailCoordenacao("coordenacao_teste_3@hotmail.com");
        
        CrudCoordenacao coordenacaoDTO = new CrudCoordenacao();
        BeanUtils.copyProperties(coordenacao, coordenacaoDTO);

        usuarioRepository.deleteAll();
        response = adminService.descadastrarCoordenacao(coordenacaoDTO);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(coordenacaoRepository.findByEmailCoordenacao(crudCoordenacao.getEmailCoordenacao()));

    }
    
    @Test
    public void testAlterarPrivilegios() {
    	CoordenacaoDTO crudCoordenacao = new CoordenacaoDTO();
        crudCoordenacao.setNomeCoordenacao("Eng Comput 4");
        crudCoordenacao.setEmailCoordenacao("coordenacao_teste_4@hotmail.com");
        crudCoordenacao.setMatriculaCoordenador(12345678);
        crudCoordenacao.setNomeCoordenador("Coordenador D");

        ResponseEntity<AdminResponse> response = adminService.cadastrarCoordenacao(crudCoordenacao);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        CadastrarUsuario usuario = new CadastrarUsuario();
        usuario.setMatricula("123456789");
        usuario.setEmail("usuario_teste@hotmail.com");
        usuario.setIdCoordenacao(coordenacaoRepository.findByEmailCoordenacao("coordenacao_teste_4@hotmail.com").getIdCoordenacao());
        usuario.setNome("francisco");
        usuario.setSenha("Francisco123*");
        
        ResponseEntity<UsuarioResponse> response1 = usuarioService.cadastrarUsuario(usuario);
		
	    assertEquals(HttpStatus.CREATED, response1.getStatusCode());
	    assertEquals("aluno", usuarioRepository.findByMatricula("123456789").get().getRole());
	    
	    CrudUsuario crudUsuario = new CrudUsuario();
	    crudUsuario.setMatricula(usuario.getMatricula());
	    crudUsuario.setRole("coord");
	    adminService.alterarPrivilegiosUsuario(crudUsuario);
	    assertEquals("coord", usuarioRepository.findByMatricula("123456789").get().getRole());
    }
    
    @Test
    public void testDeletarUsuario() {
    	CoordenacaoDTO crudCoordenacao = new CoordenacaoDTO();
        crudCoordenacao.setNomeCoordenacao("Eng Comput 5");
        crudCoordenacao.setEmailCoordenacao("coordenacao_teste_5@hotmail.com");
        crudCoordenacao.setMatriculaCoordenador(12345678);
        crudCoordenacao.setNomeCoordenador("Coordenador E");

        ResponseEntity<AdminResponse> response = adminService.cadastrarCoordenacao(crudCoordenacao);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        CadastrarUsuario usuario = new CadastrarUsuario();
        usuario.setMatricula("123456789123");
        usuario.setEmail("usuario_teste2@hotmail.com");
        usuario.setIdCoordenacao(coordenacaoRepository.findByEmailCoordenacao("coordenacao_teste_5@hotmail.com").getIdCoordenacao());
        usuario.setNome("francisco 2");
        usuario.setSenha("Francisco123#");
        
        ResponseEntity<UsuarioResponse> response1 = usuarioService.cadastrarUsuario(usuario);
		
	    assertEquals(HttpStatus.CREATED, response1.getStatusCode());
	    
	    UsuarioList usuarioList = new UsuarioList();
	    BeanUtils.copyProperties(usuario, usuarioList);
	    
	    ResponseEntity<AdminResponse> response2 = adminService.deletarUsuario(usuarioList);
	    
	    assertEquals(HttpStatus.OK, response2.getStatusCode());

    }
    
	
}
