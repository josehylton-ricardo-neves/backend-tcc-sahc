package br.edu.ifpb.sahc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.sahc.model.Coordenacao;

@Repository
public interface CoordenacaoRepository extends JpaRepository<Coordenacao, Long>{

	@Transactional
	@Modifying
	@Query("UPDATE coordenacao SET emailCoordenacao = :emailCoordenacao, nomeCoordenacao = :nomeCoordenacao, matriculaCoordenador = :matriculaCoordenador, nomeCoordenador = :nomeCoordenador WHERE idCoordenacao = :idCoordenacao")
	public int updateById(Long idCoordenacao, String nomeCoordenacao, String emailCoordenacao, long matriculaCoordenador, String nomeCoordenador);

	public Coordenacao findByEmailCoordenacao(String emailCoordenacao);
	
}