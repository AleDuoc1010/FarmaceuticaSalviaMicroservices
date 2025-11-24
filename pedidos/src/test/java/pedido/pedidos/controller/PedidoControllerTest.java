package pedido.pedidos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import pedido.pedidos.dto.AgregarItemDto;
import pedido.pedidos.dto.PedidoResponseDto;
import pedido.pedidos.model.Estado;
import pedido.pedidos.security.JwtTokenProvider; 
import pedido.pedidos.service.PedidoService;

@WebMvcTest(controllers = PedidoController.class)
@AutoConfigureMockMvc(addFilters = false)
class PedidoControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private PedidoService pedidoService;
    @MockBean private JwtTokenProvider jwtTokenProvider;

    @Test
    void agregarItem_DebeRetornar200_CuandoEsValido() throws Exception {
        AgregarItemDto dto = new AgregarItemDto("SKU-123", 2);
        PedidoResponseDto respuestaSimulada = new PedidoResponseDto(
                1L, "uuid-ped", BigDecimal.TEN, Estado.PENDIENTE, new ArrayList<>()
        );

        when(pedidoService.agregarItem(any(), any(AgregarItemDto.class)))
            .thenReturn(respuestaSimulada);

        UsernamePasswordAuthenticationToken auth = 
            new UsernamePasswordAuthenticationToken("uuid-usuario-logueado", null);

        mockMvc.perform(post("/pedidos/carrito")
                .principal(auth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}