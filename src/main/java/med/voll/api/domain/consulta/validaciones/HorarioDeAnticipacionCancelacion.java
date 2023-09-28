package med.voll.api.domain.consulta.validaciones;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosCancelarConsulta;

@Component
public class HorarioDeAnticipacionCancelacion implements ValidadorDeCancelacion{

    @Autowired
    private ConsultaRepository consultaRepository;

    @Override
    public void validar(DatosCancelarConsulta datos) {

        var consulta = consultaRepository.getReferenceById(datos.idConsulta());
        var ahora = LocalDateTime.now();
        var diferenciaEnHoras = Duration.between(ahora, consulta.getFecha()).toHours();

        if (diferenciaEnHoras < 24) {
            throw new ValidationException("La consulta solo puede cancelarse con mas de 24 horas de anticipacion");
        }
    }

}
