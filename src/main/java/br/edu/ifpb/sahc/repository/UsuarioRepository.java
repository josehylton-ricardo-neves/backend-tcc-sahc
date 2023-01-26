package br.edu.ifpb.sahc.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.sahc.model.UsuarioModel;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, UUID>{

	//public Optional<User> findByMatricula(String matricula);
	
	public Optional<UsuarioModel> findByRegistry(String registry);
	
	public Optional<UsuarioModel> findByEmail(String email);
	
	public boolean existsByRegistry(String registry);
	
	@Transactional
	@Modifying
	//@Query("UPDATE User u SET u.password = :password WHERE u.registry = :registry")
	@Query("UPDATE Usuario SET senha = :matricula WHERE registry = :senha")
	public int updatePassword(@Param("matricula") String matricula, @Param("senha") String senha);
}
