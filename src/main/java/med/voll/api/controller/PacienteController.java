package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.paciente.DatosActualizarPaciente;
import med.voll.api.domain.paciente.DatosListadoPaciente;
import med.voll.api.domain.paciente.DatosRegistroPaciente;
import med.voll.api.domain.paciente.DatosRespuestaPaciente;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;

@RestController
@RequestMapping("/pacientes")
@SecurityRequirement(name = "bearer-key")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping
    @Transactional
    @Operation(summary = "Registra un nuevo paciente en la base de datos")
    public ResponseEntity<DatosRespuestaPaciente> registrarPaciente(@RequestBody @Valid DatosRegistroPaciente datos, UriComponentsBuilder uriBuilder) {
        System.out.println("Paciente registrado!");
        Paciente paciente = pacienteRepository.save(new Paciente(datos));
        var datosRespuesta = new DatosRespuestaPaciente(paciente.getId(), paciente.getNombre(), paciente.getEmail(),
                paciente.getTelefono(), paciente.getDocumento(), new DatosDireccion(paciente.getDireccion().getCalle(),
                        paciente.getDireccion().getDistrito(), paciente.getDireccion().getCiudad(),
                        paciente.getDireccion().getNumero(), paciente.getDireccion().getComplemento()));
        var url = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuesta);
    }

    @GetMapping
    @Operation(summary = "Lista todos los pacientes registrados en la base de datos")
    public ResponseEntity<Page<DatosListadoPaciente>> listarPacientes(@PageableDefault(size = 10) Pageable paginacion) {
        return ResponseEntity.ok(pacienteRepository.findByActivoTrue(paginacion).map(DatosListadoPaciente::new));
    }

    @PutMapping
    @Transactional
    @Operation(summary = "Actualiza la información de un paciente registrado en la base de datos")
    public ResponseEntity<DatosRespuestaPaciente> actualizarPaciente(@RequestBody @Valid DatosActualizarPaciente datos) {
        Paciente paciente = pacienteRepository.getReferenceById(datos.id());
        paciente.actualizarInformacion(datos);
        var datosRespuesta = new DatosRespuestaPaciente(paciente.getId(), paciente.getNombre(), paciente.getEmail(),
                paciente.getTelefono(), paciente.getDocumento(), new DatosDireccion(paciente.getDireccion().getCalle(),
                        paciente.getDireccion().getDistrito(), paciente.getDireccion().getCiudad(),
                        paciente.getDireccion().getNumero(), paciente.getDireccion().getComplemento()));
        return ResponseEntity.ok(datosRespuesta);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Elimina un paciente registrado en la base de datos (inactiva)")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        Paciente paciente = pacienteRepository.getReferenceById(id);
        paciente.inactivarPaciente();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retorna los datos de un paciente registrado en la base de datos con ID dado")
    public ResponseEntity<DatosRespuestaPaciente> retornaDatosPaciente(@PathVariable Long id){
        Paciente paciente = pacienteRepository.getReferenceById(id);
        var datosRespuesta = new DatosRespuestaPaciente(paciente.getId(), paciente.getNombre(), paciente.getEmail(),
                paciente.getTelefono(), paciente.getDocumento(), new DatosDireccion(paciente.getDireccion().getCalle(),
                        paciente.getDireccion().getDistrito(), paciente.getDireccion().getCiudad(),
                        paciente.getDireccion().getNumero(), paciente.getDireccion().getComplemento()));
        return ResponseEntity.ok(datosRespuesta);
    }
}
