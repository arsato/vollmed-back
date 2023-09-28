package med.voll.api.domain.consulta;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.paciente.Paciente;

@Table(name = "consultas")
@Entity(name = "Consulta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="medico_id")
    private Medico medico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="paciente_id")
    private Paciente paciente;

    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_cancelamiento")
    private MotivoCancelamiento motivoCancelamiento;

    public void cancelarConsulta(MotivoCancelamiento motivo) {
        this.motivoCancelamiento = motivo;
    }

    public Consulta( Medico medico, Paciente paciente, LocalDateTime fecha) {
        this.medico = medico;
        this.paciente = paciente;
        this.fecha = fecha;
    }
}
