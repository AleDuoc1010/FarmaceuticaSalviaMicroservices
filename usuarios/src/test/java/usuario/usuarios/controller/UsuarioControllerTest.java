package usuario.usuarios.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import usuario.usuarios.dto.RegistroDto;
import usuario.usuarios.model.Usuario;
import usuario.usuarios.service.UsuarioService;
import usuario.usuarios.security.JwtTokenProvider;

@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false) 
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registrarUsuario_DebeRetornar201_CuandoDatosValidos() throws Exception {

        RegistroDto dto = new RegistroDto("Pepito", "pepito@mail.com", "123", "pass");
        
        Usuario usuarioSimulado = new Usuario();
        usuarioSimulado.setUuid("uuid-123");
        usuarioSimulado.setNombre("Pepito");
        

        when(usuarioService.registrarUsuario(any(RegistroDto.class))).thenReturn(usuarioSimulado);


        mockMvc.perform(post("/usuarios/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))) 
                .andExpect(status().isCreated()) 
                .andExpect(jsonPath("$.uuid").value("uuid-123"));
    }
}