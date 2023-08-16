package br.edu.ifpb.sahc.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.sahc.model.Coordenacao;
import br.edu.ifpb.sahc.model.Horario;
import br.edu.ifpb.sahc.model.HorarioID;
import br.edu.ifpb.sahc.model.Usuario;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, HorarioID>{

	public List<Optional<Horario>> findAllByIdCoordenacaoAndMatriculaUsuario(Coordenacao coordenacao, Optional<Usuario> usuario);
	
	
	@Query(value = "SELECT hora_inicial, hora_final, id_coordenacao, horario_agendado, matricula_usuario FROM horario WHERE id_coordenacao = :idCoordenacao AND horario_agendado = false AND hora_inicial > SYSDATE()", nativeQuery = true)
	public List<Optional<Horario>> findAllByIdCoordenacao(Coordenacao idCoordenacao);
	
	
	public List<Optional<Horario>> findAllByIdCoordenacaoAndHorarioAgendado(Coordenacao coordenacao, boolean horarioAgendado);
	
	
	public Optional<Horario> findByIdCoordenacaoAndHoraInicialAndHoraFinalAndHorarioAgendado(Coordenacao coordenacao,
			Timestamp horaInicial, Timestamp horaFinal, boolean horarioAgendado);
	
	
	public Optional<Horario> findByIdCoordenacaoAndHoraInicialAndHoraFinalAndMatriculaUsuarioAndHorarioAgendado(
			Coordenacao idCoordenacao, Timestamp horaInicial, Timestamp horaFinal, Optional<Usuario> usuario, boolean horarioAgendado);
	
	
	@Transactional
	@Modifying
	@Query("UPDATE horario SET horarioAgendado = true, matriculaUsuario = :usuario WHERE idCoordenacao = :coordenacao AND horaInicial = :horaInicial AND horaFinal = :horaFinal AND horarioAgendado = false")
	public int agendarHorarioDisponivel(Coordenacao coordenacao, Timestamp horaInicial, Timestamp horaFinal, Optional<Usuario> usuario);
	
	
	@Transactional
	@Modifying
	@Query("UPDATE horario SET horarioAgendado = false, matriculaUsuario = null WHERE idCoordenacao = :coordenacao AND horaInicial = :horaInicial AND horaFinal = :horaFinal AND matriculaUsuario = :usuario AND horarioAgendado = true")
	public int desagendarHorarioDisponivel(Coordenacao coordenacao, Timestamp horaInicial, Timestamp horaFinal, Optional<Usuario> usuario);
	

	@Transactional
	@Modifying
	@Query("DELETE FROM horario WHERE idCoordenacao = :idCoordenacao AND horaInicial = :horaInicial AND horaFinal = :horaFinal")
	public int apagarHorarioCadastrado(Coordenacao idCoordenacao, Timestamp horaInicial, Timestamp horaFinal);


	@Query(value = "SELECT h FROM horario h WHERE h.idCoordenacao = :idCoordenacao AND ((h.horarioId.horaInicial >= :horaInicial AND h.horarioId.horaInicial < :horaFinal) OR (h.horarioId.horaFinal > :horaInicial AND h.horarioId.horaFinal <= :horaFinal) OR (h.horarioId.horaInicial <= :horaInicial AND h.horarioId.horaFinal >= :horaFinal))")
	public List<Optional<Horario>> findHorariosConflitantes(Coordenacao idCoordenacao, Timestamp horaInicial, Timestamp horaFinal);

	
	@Transactional
	@Modifying
	@Query("DELETE FROM horario WHERE idCoordenacao = :coordenacao")
	public void deleteAllByIdCoordenacao(Coordenacao coordenacao);
}
