package med.voll.api.domain.consulta;

import jakarta.validation.constraints.NotNull;

public record DatosCancelarConsulta(Long idConsulta, @NotNull MotivoCancelamiento motivoCancelamiento) {

}
