package inventario.inventario.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import inventario.inventario.dto.InventarioDto;
import inventario.inventario.security.JwtTokenProvider;
import inventario.inventario.service.InventarioService;

@WebMvcTest(controllers = InventarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class InventarioControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private InventarioService inventarioService;
    @MockBean private JwtTokenProvider jwtTokenProvider;

    @Test
    void obtenerStock_DebeRetornar200_ConCantidad() throws Exception {
        InventarioDto dto = new InventarioDto("SKU-Z", 50);
        when(inventarioService.obtenerStock("SKU-Z")).thenReturn(dto);

        mockMvc.perform(get("/inventario/SKU-Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(50));
    }
}