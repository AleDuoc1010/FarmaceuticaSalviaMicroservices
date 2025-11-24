package producto.productos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import producto.productos.dto.ProductoCreateDto;
import producto.productos.dto.ProductoResponseDto;
import producto.productos.exception.SkuAlreadyExistsException;
import producto.productos.model.Producto;
import producto.productos.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock private ProductoRepository productoRepository;
    @InjectMocks private ProductoService productoService;

    @Test
    void crearProducto_DebeLanzarExcepcion_SiSkuExiste() {
        ProductoCreateDto dto = new ProductoCreateDto(
            "SKU-EXISTENTE", "Prod", "Desc", BigDecimal.TEN, true, false, "img"
        );
        when(productoRepository.existsBySku("SKU-EXISTENTE")).thenReturn(true);

        assertThrows(SkuAlreadyExistsException.class, () -> {
            productoService.crearProducto(dto);
        });

        verify(productoRepository, never()).save(any());
    }

    @Test
    void crearProducto_DebeGuardar_SiSkuEsNuevo() {
        ProductoCreateDto dto = new ProductoCreateDto(
            "SKU-NUEVO", "Prod", "Desc", BigDecimal.TEN, true, false, "img"
        );
        when(productoRepository.existsBySku("SKU-NUEVO")).thenReturn(false);
        
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> {
            Producto p = (Producto) i.getArguments()[0];
            p.setId(1L);
            return p;
        });

        ProductoResponseDto resultado = productoService.crearProducto(dto);

        assertNotNull(resultado);
        assertEquals("SKU-NUEVO", resultado.sku());
        verify(productoRepository).save(any(Producto.class));
    }
}