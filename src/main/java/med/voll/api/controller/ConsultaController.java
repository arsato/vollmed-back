package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.consulta.AgendaDeConsultaService;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.consulta.DatosCancelarConsulta;
import med.voll.api.domain.consulta.DatosDetalleConsulta;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private AgendaDeConsultaService agendaDeConsultaService;

    @Autowired
    private ConsultaRepository consultaRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosDetalleConsulta> agendar(@RequestBody @Valid DatosAgendarConsulta datos) {
        System.out.println(datos);
        var response = agendaDeConsultaService.agendar(datos);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<DatosDetalleConsulta>> listarConsultas(@PageableDefault(size = 10) Pageable paginacion) {
        return ResponseEntity.ok(consultaRepository.findByMotivoCancelamientoNull(paginacion).map(DatosDetalleConsulta::new));
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<Void> cancelarConsulta(@RequestBody @Valid DatosCancelarConsulta datos) {
        agendaDeConsultaService.cancelar(datos);
        System.out.println("Consulta eliminada!");
        return ResponseEntity.noContent().build();
    }

    
}
