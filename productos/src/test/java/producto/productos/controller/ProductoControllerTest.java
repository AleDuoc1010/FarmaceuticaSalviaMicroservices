package producto.productos.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import producto.productos.dto.ProductoResponseDto;
import producto.productos.security.JwtTokenProvider;
import producto.productos.service.ProductoService;

@WebMvcTest(controllers = ProductoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductoControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private ProductoService productoService;
    @MockBean private JwtTokenProvider jwtTokenProvider;

    @Test
    void getProductoBySku_DebeRetornar200_Y_JsonCorrecto() throws Exception {
        ProductoResponseDto mockProd = new ProductoResponseDto(
            1L, "SKU-A", "Paracetamol", "Desc", BigDecimal.TEN, "img", false, false, null
        );
        when(productoService.findBySku("SKU-A")).thenReturn(mockProd);

        mockMvc.perform(get("/productos/SKU-A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Paracetamol"));
    }
}