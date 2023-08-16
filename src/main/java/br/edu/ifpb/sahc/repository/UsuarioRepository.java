package br.edu.ifpb.sahc.repository;

import java.util.Optional;
import java.util.UUID;

import br.edu.ifpb.sahc.model.Coordenacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ifpb.sahc.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID>{

	public Optional<Usuario> findByMatricula(String matricula);
	
	public Optional<Usuario> findCoordenacaoByMatricula(String matricula);
	
	public Optional<Usuario> findByEmail(String email);
	
	public boolean existsByMatricula(String matricula);
	
	@Transactional
	@Modifying
	@Query("UPDATE usuario SET senha = :senha WHERE matricula = :matricula")
	public int updateSenha(@Param("matricula") String matricula, @Param("senha") String senha);
	
	@Transactional
	@Modifying
	@Query("UPDATE usuario SET role = :role WHERE matricula = :matricula")
	public int alterarPrivilegioUsuario(String matricula, String role);

	@Transactional
	@Modifying
	@Query("DELETE FROM  usuario WHERE idCoordenacao = :coordenacao")
	public void deleteAllByIdCoordenacao(Coordenacao coordenacao);

}
