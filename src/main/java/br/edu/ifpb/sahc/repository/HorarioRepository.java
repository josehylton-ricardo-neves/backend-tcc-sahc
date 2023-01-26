package br.edu.ifpb.sahc.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.sahc.model.HorarioModel;

public interface HorarioRepository extends JpaRepository<HorarioModel, UUID>{

	//@Transactional
	//@Modifying
	//@Query("UPDATE User u SET u.password = :password WHERE u.registry = :registry")
	@Query("SELECT * FROM Horario WHERE nomeCoordenacao = :coordenacao AND horarioAgendado = false AND horarioDisponivel > SYSDATE")
	public Optional<List<HorarioModel>> findAllByNomeCoordenacao(String coordenacao);
	
	@Transactional
	@Modifying
	@Query("INSERT INTO Horario (idCoordenacao, nomeCoordenacao, horarioDisponivel, horarioAgendado, matriculaDiscente) VALUES (:CoordinationId, :CoordinationName, true, :scheduledTime, null)")
	public int createScheduleByCoordination(String idCoordenacao, String nomeCoordenacao, Date horarioDisponivel);
}
