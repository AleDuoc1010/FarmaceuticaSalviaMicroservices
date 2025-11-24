package inventario.inventario.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import inventario.inventario.dto.InventarioDto;
import inventario.inventario.exception.StockInsuficienteException;
import inventario.inventario.model.Inventario;
import inventario.inventario.repository.InventarioRepository;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock private InventarioRepository inventarioRepository;
    @InjectMocks private InventarioService inventarioService;

    @Test
    void reducirStock_DebeLanzarExcepcion_SiStockEsMenorAlPedido() {
        String sku = "SKU-1";
        Inventario inv = new Inventario();
        inv.setSku(sku);
        inv.setCantidad(5);

        when(inventarioRepository.findBySku(sku)).thenReturn(Optional.of(inv));

        assertThrows(StockInsuficienteException.class, () -> {
            inventarioService.reducirStock(sku, 10);
        });
        
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    void reducirStock_DebeRestar_SiHaySuficiente() {
        String sku = "SKU-1";
        Inventario inv = new Inventario();
        inv.setSku(sku);
        inv.setCantidad(10);

        when(inventarioRepository.findBySku(sku)).thenReturn(Optional.of(inv));
        when(inventarioRepository.save(any(Inventario.class))).thenAnswer(i -> i.getArguments()[0]);

        InventarioDto resultado = inventarioService.reducirStock(sku, 3);

        assertEquals(7, resultado.cantidad());
        verify(inventarioRepository).save(any(Inventario.class));
    }
}