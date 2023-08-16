package br.edu.ifpb.sahc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import br.edu.ifpb.sahc.dto.admin.AdminResponse;
import br.edu.ifpb.sahc.dto.admin.CoordenacaoDTO;
import br.edu.ifpb.sahc.dto.admin.CrudCoordenacao;
import br.edu.ifpb.sahc.dto.coordenacao.CoordenacaoResponse;
import br.edu.ifpb.sahc.dto.usuario.CadastrarUsuario;
import br.edu.ifpb.sahc.model.Coordenacao;
import br.edu.ifpb.sahc.repository.CoordenacaoRepository;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoordenacaoServiceTest {

	@Autowired
	private CoordenacaoRepository coordenacaoRepository;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private CoordenacaoService coordenacaoService;
	@Autowired
	private HorarioService horarioService;
	@Autowired
	private AdminService adminService;

	@BeforeAll
	public void setUp() {
		CoordenacaoDTO crudCoordenacao = new CoordenacaoDTO();
		crudCoordenacao.setNomeCoordenacao("Engenharia");
		crudCoordenacao.setEmailCoordenacao("coord_eng@hotmail.com");

		adminService.cadastrarCoordenacao(crudCoordenacao);

		Long idCoord = coordenacaoRepository.findByEmailCoordenacao(crudCoordenacao.getEmailCoordenacao()).getIdCoordenacao();

		CadastrarUsuario usuario = new CadastrarUsuario();
		usuario.setMatricula("12345");
		usuario.setNome("joao");
		usuario.setEmail("joao@hotmail.com");
		usuario.setSenha("Joao12345@");
		usuario.setIdCoordenacao(idCoord);

		usuarioService.cadastrarUsuario(usuario);

	}

	@Test
	public void testConsultarCoordenacao() {

		List<Coordenacao> coordenacoes = coordenacaoService.consultarCoordenacao();
		assertTrue(!coordenacoes.isEmpty());
	}

	@Test
	public void testCadastrarHorarioDisponivelSucesso() {
		String horarios1 = "[{\"horaInicial\": \"2023-05-03 09:00:00\", \"horaFinal\": \"2023-05-03 10:00:00\"}]";
		String horarios2 = "[{\"horaInicial\": \"2023-05-03 10:00:00\", \"horaFinal\": \"2023-05-03 11:00:00\"}, "
				+ "{\"horaInicial\": \"2023-05-03 10:00:00\", \"horaFinal\": \"2023-05-03 11:00:00\"}]";
		ResponseEntity<CoordenacaoResponse> response1 = coordenacaoService.cadastrarHorarioDisponivel(horarios1, "12345");
		ResponseEntity<CoordenacaoResponse> response2 = coordenacaoService.cadastrarHorarioDisponivel(horarios2, "12345");

		assertEquals(HttpStatus.CREATED, response1.getStatusCode());
		assertEquals("Horario(s) cadastrado(s) com sucesso", response1.getBody().getMensagem());

		assertEquals(HttpStatus.CREATED, response2.getStatusCode());
		assertEquals("Horario(s) cadastrado(s) com sucesso", response2.getBody().getMensagem());

	}

	@Test
	public void testCadastrarHorarioDisponivelFalha() {

		String horarios1 = "[{\"horaInicial\": \"2023-05-03 13:00:00\", \"horaFinal\": \"2023-05-03 14:00:00\"}]";
		String horarios2 = "[{\"horaInicial\": \"2023-05-03 12:30:00\", \"horaFinal\": \"2023-05-03 13:30:00\"}]";
		String horarios3 = "[{\"horaInicial\": \"2023-05-03 13:30:00\", \"horaFinal\": \"2023-05-03 14:30:00\"}]";
		String horarios4 = "[{\"horaInicial\": \"2023-05-03 13:15:00\", \"horaFinal\": \"2023-05-03 13:45:00\"}]";
		String horarios5 = "[{\"horaInicial\": \"2023-05-03 12:30:00\", \"horaFinal\": \"2023-05-03 14:30:00\"}]";

		String horarios6 = "[{{\"horaInicial\": \"2023-05-03 17:00:00\", \"horaFinal\": \"2023-05-03 18:00:00\"}]";
		String horarios7 = "[{\"horaInial\": \"2023-05-03 07:00:00\", \"horaFinal\": \"2023-05-03 18:00:00\"}]";
		String horarios8 = "";
		String horarios9 = null;

		ResponseEntity<CoordenacaoResponse> response1 = coordenacaoService.cadastrarHorarioDisponivel(horarios1, "12345");
		ResponseEntity<CoordenacaoResponse> response2 = coordenacaoService.cadastrarHorarioDisponivel(horarios2, "12345");
		ResponseEntity<CoordenacaoResponse> response3 = coordenacaoService.cadastrarHorarioDisponivel(horarios3, "12345");
		ResponseEntity<CoordenacaoResponse> response4 = coordenacaoService.cadastrarHorarioDisponivel(horarios4, "12345");
		ResponseEntity<CoordenacaoResponse> response5 = coordenacaoService.cadastrarHorarioDisponivel(horarios5, "12345");
		ResponseEntity<CoordenacaoResponse> response6 = coordenacaoService.cadastrarHorarioDisponivel(horarios6, "12345");
		ResponseEntity<CoordenacaoResponse> response7 = coordenacaoService.cadastrarHorarioDisponivel(horarios7, "12345");
		ResponseEntity<CoordenacaoResponse> response8 = coordenacaoService.cadastrarHorarioDisponivel(horarios8, "12345");

		assertEquals(HttpStatus.CREATED, response1.getStatusCode());
		assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());
		assertEquals(HttpStatus.CONFLICT, response3.getStatusCode());
		assertEquals(HttpStatus.CONFLICT, response4.getStatusCode());
		assertEquals(HttpStatus.CONFLICT, response5.getStatusCode());

		assertEquals(HttpStatus.BAD_REQUEST, response6.getStatusCode());
		assertEquals(HttpStatus.BAD_REQUEST, response7.getStatusCode());
		assertEquals(HttpStatus.BAD_REQUEST, response8.getStatusCode());

		assertThrows(IllegalArgumentException.class, () -> {coordenacaoService.descadastrarHorarioDisponivel(horarios9, "12345");});

	}

	@Test
	public void testDescadastrarHorarioDisponivelSucesso() {

		String horarios = "[{\"horaInicial\": \"2023-05-03 09:00:00\", \"horaFinal\": \"2023-05-03 10:00:00\"}]";

		ResponseEntity<CoordenacaoResponse> response = coordenacaoService.descadastrarHorarioDisponivel(horarios, "12345");

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Horario(s) descadastrado(s) com sucesso", response.getBody().getMensagem());
	}

	@Test
	public void testDescadastrarHorarioDisponivelFalha() {

		String horarios1 = "[{{\"horaInicial\": \"2023-05-03 09:00:00\", \"horaFinal\": \"2023-05-03 10:00:00\"}]";
		String horarios2 = "[{\"horaInial\": \"2023-05-03 09:00:00\", \"horaFinal\": \"2023-05-03 10:00:00\"}]";
		String horarios3 = "[{\"horaInicial\": \"2023-05-03 09:00:00\"}]";
		String horarios4 = "[{\"horaInicial\": \"2023-05-40 09:00:00\", \"horaFinal\": \"2023-05-03 30:00:00\"}]";
		String horarios5 = "";
		String horarios6 = null;

		assertThrows(NullPointerException.class, () -> {coordenacaoService.descadastrarHorarioDisponivel(horarios1, "12345");});
		assertThrows(NullPointerException.class, () -> {coordenacaoService.descadastrarHorarioDisponivel(horarios2, "12345");});
		assertThrows(IllegalArgumentException.class, () -> {coordenacaoService.descadastrarHorarioDisponivel(horarios3, "12345");});
		assertThrows(IllegalArgumentException.class, () -> {coordenacaoService.descadastrarHorarioDisponivel(horarios4, "12345");});
		assertThrows(NullPointerException.class, () -> {coordenacaoService.descadastrarHorarioDisponivel(horarios5, "12345");});
		assertThrows(IllegalArgumentException.class, () -> {coordenacaoService.descadastrarHorarioDisponivel(horarios6, "12345");});

	}

	@Test
	public void testUpdateById() {

		CoordenacaoDTO crudCoordenacao = new CoordenacaoDTO();
		crudCoordenacao.setNomeCoordenacao("Engenharia comp");
		crudCoordenacao.setEmailCoordenacao("coord_eng_comp@hotmail.com");

		ResponseEntity<AdminResponse> response = adminService.cadastrarCoordenacao(crudCoordenacao);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		Long idCoord = coordenacaoRepository.findByEmailCoordenacao(crudCoordenacao.getEmailCoordenacao()).getIdCoordenacao();

		CrudCoordenacao coordenacao = new CrudCoordenacao();

		coordenacao.setIdCoordenacao(idCoord);
		coordenacao.setNomeCoordenacao("engen");
		coordenacao.setEmailCoordenacao("coord_eng3@hotmail.com");

		coordenacaoService.updateById(coordenacao);

		assertEquals("engen", coordenacaoRepository.findById(idCoord).get().getNomeCoordenacao());
		assertEquals("coord_eng3@hotmail.com", coordenacaoRepository.findById(idCoord).get().getEmailCoordenacao());

	}

	@Test
	public void testDelete() {

		CoordenacaoDTO crudCoordenacao = new CoordenacaoDTO();
		crudCoordenacao.setNomeCoordenacao("Engenharia2");
		crudCoordenacao.setEmailCoordenacao("coord_eng2@hotmail.com");

		ResponseEntity<AdminResponse> response = adminService.cadastrarCoordenacao(crudCoordenacao);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		Coordenacao coordenacao = coordenacaoRepository.findByEmailCoordenacao(crudCoordenacao.getEmailCoordenacao());

		usuarioService.deleteAllByIdCoordenacao(coordenacao);
		horarioService.deleteAllByIdCoordenacao(coordenacao);
		coordenacaoService.delete(coordenacao);
		coordenacaoRepository.findByEmailCoordenacao(crudCoordenacao.getEmailCoordenacao());
		
		assertNull(coordenacaoRepository.findByEmailCoordenacao(crudCoordenacao.getEmailCoordenacao()));

	}

}
