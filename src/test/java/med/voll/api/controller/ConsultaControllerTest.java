package med.voll.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import med.voll.api.domain.consulta.AgendaDeConsultaService;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.consulta.DatosDetalleConsulta;
import med.voll.api.domain.medico.Especialidad;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DatosAgendarConsulta> jsonAgendarConsulta;

    @Autowired
    private JacksonTester<DatosDetalleConsulta> jsonDetalleConsulta;

    @MockBean
    private AgendaDeConsultaService agendaDeConsultaService;

    @Test
    @DisplayName("Deberia retornar estado http 400 cuando los datos ingresados sean invalidos")
    @WithMockUser
    void testAgendar1() throws Exception{
        var response = mvc.perform(post("/consultas")).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @DisplayName("Deberia retornar estado http 200 cuando los datos ingresados son validos")
    @WithMockUser
    void testAgendar2() throws Exception{

        var fecha = LocalDateTime.now().plusHours(1);
        var especialidad = Especialidad.ORTOPEDIA;
        var datos = new DatosDetalleConsulta(null, 2l, 5l, fecha);
        when(agendaDeConsultaService.agendar(any())).thenReturn(datos);

        var response = mvc.perform(post("/consultas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonAgendarConsulta.write(new DatosAgendarConsulta(null, 2l,5l,fecha,especialidad)).getJson()))
                                    .andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        var jsonEsperado = jsonDetalleConsulta.write(datos).getJson();

        assertEquals(response.getContentAsString(), jsonEsperado);
    }

}