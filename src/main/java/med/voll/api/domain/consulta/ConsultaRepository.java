package med.voll.api.domain.consulta;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long>{

    Page<Consulta> findAll(Pageable paginacion);

    Boolean existsByPacienteIdAndFechaBetween(Long idPaciente, LocalDateTime primerHorario, LocalDateTime ultimoHorario);

    Boolean existsByMedicoIdAndFecha(Long idMedico, LocalDateTime fecha);

    Page<Consulta> findByMotivoCancelamientoNull(Pageable paginacion);
    
}