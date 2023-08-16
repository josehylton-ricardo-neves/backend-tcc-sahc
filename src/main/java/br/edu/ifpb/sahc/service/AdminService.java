package br.edu.ifpb.sahc.service;

import br.edu.ifpb.sahc.dto.admin.CoordenacaoDTO;
import br.edu.ifpb.sahc.dto.usuario.UsuarioList;
import br.edu.ifpb.sahc.model.Usuario;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.edu.ifpb.sahc.dto.admin.AdminResponse;
import br.edu.ifpb.sahc.dto.admin.CrudCoordenacao;
import br.edu.ifpb.sahc.dto.admin.CrudUsuario;
import br.edu.ifpb.sahc.model.Coordenacao;

@Service
public class AdminService {

	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private CoordenacaoService coordenacaoService;
	@Autowired
	private Usuario usuario;
	@Autowired
	private Coordenacao coordenacao;
	
	public ResponseEntity<AdminResponse> cadastrarCoordenacao(CoordenacaoDTO coordenacaoDTO) {
		BeanUtils.copyProperties(coordenacaoDTO, coordenacao);
		coordenacaoService.save(coordenacao);
		return responseEntity(null, null, HttpStatus.CREATED);
	}
	
	public ResponseEntity<AdminResponse> atualizarCoordenacao(CrudCoordenacao coordenacao) {

		int update = coordenacaoService.updateById(coordenacao);
		if(update == 1) {
			return responseEntity(null, null, HttpStatus.OK);			
		}
		return responseEntity(null, null, HttpStatus.BAD_REQUEST);
	}
	
	public ResponseEntity<AdminResponse> descadastrarCoordenacao(CrudCoordenacao coordenacaoDTO) {
		BeanUtils.copyProperties(coordenacaoDTO, coordenacao);
		
		try{
			usuarioService.deleteAllByIdCoordenacao(coordenacao);
			coordenacaoService.delete(coordenacao);

		}catch (Exception ex){
			return responseEntity(null, null, HttpStatus.BAD_REQUEST);

		}
		return responseEntity(null, null, HttpStatus.CREATED);
	}

	public ResponseEntity<AdminResponse> alterarPrivilegiosUsuario(CrudUsuario usuario) {
		int update = usuarioService.alterarPrivilegioUsuario(usuario);
		if(update == 1) {
			return responseEntity(null, null, HttpStatus.CREATED);
		}
		return responseEntity(null, null, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<AdminResponse> deletarUsuario(UsuarioList usuarioList) {
		BeanUtils.copyProperties(usuarioList, usuario);

		try {
			usuarioService.delete(usuario);
		}catch (Exception ex){
			return responseEntity(null, null, HttpStatus.BAD_REQUEST);

		}
		return responseEntity(null, null, HttpStatus.OK);

	}

	private ResponseEntity<AdminResponse> responseEntity(Integer status, String mensagem, HttpStatus httpStatus) {
		return new ResponseEntity<AdminResponse>(httpStatus);
	}
}
