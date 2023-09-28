package med.voll.api.domain.consulta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import med.voll.api.domain.consulta.validaciones.ValidadorDeCancelacion;
import med.voll.api.domain.consulta.validaciones.ValidadorDeConsultas;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errores.ValidacionDeIntegridad;

@Service
public class AgendaDeConsultaService {

    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    List<ValidadorDeConsultas> validadores;

    @Autowired
    List<ValidadorDeCancelacion> validadoresCancelacion;

    public DatosDetalleConsulta agendar(DatosAgendarConsulta datos) {

        if (!pacienteRepository.findById(datos.idPaciente()).isPresent()) {
            throw new ValidacionDeIntegridad("Este id de paciente no existe");
        }

        if (datos.idMedico() != null && !medicoRepository.existsById(datos.idMedico())) {
            throw new ValidacionDeIntegridad("El medico no existe");
        }

        validadores.forEach(validador -> validador.validar(datos));

        var paciente = pacienteRepository.findById(datos.idPaciente()).get();

        var medico = seleccionarMedico(datos);

        if (medico == null) {
            throw new ValidacionDeIntegridad("No existen medicos disponibles para este horario y especialidad");
        }


        var consulta = new Consulta(medico, paciente, datos.fecha());

        consultaRepository.save(consulta);

        return new DatosDetalleConsulta(consulta);
    }

    public void cancelar(DatosCancelarConsulta datos) {
        if(!consultaRepository.existsById(datos.idConsulta())){
            throw new ValidacionDeIntegridad("La consulta no existe");
        }

        validadoresCancelacion.forEach(validador -> validador.validar(datos));

        var consulta = consultaRepository.getReferenceById(datos.idConsulta());
        consulta.cancelarConsulta(datos.motivoCancelamiento());
    }

    private Medico seleccionarMedico(DatosAgendarConsulta datos) {
        if (datos.idMedico() != null) {
            return medicoRepository.getReferenceById(datos.idMedico());
        }
        if (datos.especialidad() == null) {
            throw new ValidacionDeIntegridad("Debe especificar una especialidad o un medico");
        }
        return medicoRepository.seleccionarMedicoPorEspecialidadDisponible(datos.especialidad(), datos.fecha());
    }
}
