package med.voll.api.domain.consulta.validaciones;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

import org.springframework.stereotype.Component;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DatosAgendarConsulta;

@Component
public class HorarioDeAnticipacion implements ValidadorDeConsultas {

    public void validar(DatosAgendarConsulta datos) {

        var ahora = LocalDateTime.now();
        Temporal horaConsulta = datos.fecha();

        var diferenciaDe30Min = Duration.between(ahora, horaConsulta).toMinutes() < 30;

        if (diferenciaDe30Min) {
            throw new ValidationException("La consulta debe ser agendada con al menos 30 minutos de anticipación");
        }
    }

}
