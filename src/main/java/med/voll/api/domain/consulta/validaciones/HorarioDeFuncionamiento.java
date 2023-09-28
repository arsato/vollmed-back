package med.voll.api.domain.consulta.validaciones;

import java.time.DayOfWeek;

import org.springframework.stereotype.Component;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DatosAgendarConsulta;

@Component
public class HorarioDeFuncionamiento implements ValidadorDeConsultas {
    public void validar(DatosAgendarConsulta datos) {
        var domingo = DayOfWeek.SUNDAY.equals(datos.fecha().getDayOfWeek());

        var antesApertura = datos.fecha().getHour() < 7;
        var despuesCierre = datos.fecha().getHour() > 19;

        if (domingo || antesApertura || despuesCierre) {
            throw new ValidationException("El horario  de antencion es de lunes a sábado de 07:00 a 19:00 horas");
        }
    }

}
