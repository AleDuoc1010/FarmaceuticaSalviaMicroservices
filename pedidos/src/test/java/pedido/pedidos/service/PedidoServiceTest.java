package pedido.pedidos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pedido.pedidos.client.CatalogoClient;
import pedido.pedidos.client.InventarioClient;
import pedido.pedidos.dto.AgregarItemDto;
import pedido.pedidos.dto.PedidoResponseDto;
import pedido.pedidos.dto.externo.InventarioExternoDto;
import pedido.pedidos.dto.externo.ProductoExternoDto;
import pedido.pedidos.exception.StockInsuficienteException;
import pedido.pedidos.model.Estado;
import pedido.pedidos.model.Pedido;
import pedido.pedidos.repository.PedidoRepository;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock private PedidoRepository pedidoRepository;
    @Mock private CatalogoClient catalogoClient;
    @Mock private InventarioClient inventarioClient;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void agregarItem_DebeLanzarExcepcion_CuandoNoHayStock() {

        String usuarioUuid = "user-1";
        AgregarItemDto dto = new AgregarItemDto("SKU-A", 10);


        when(pedidoRepository.findByUsuarioUuidAndEstado(usuarioUuid, Estado.PENDIENTE))
            .thenReturn(Optional.empty());


        when(catalogoClient.getProductoBySku("SKU-A"))
            .thenReturn(new ProductoExternoDto("SKU-A", "Prod A", "Desc", BigDecimal.TEN, false, "url"));

        when(inventarioClient.obtenerStock("SKU-A"))
            .thenReturn(new InventarioExternoDto("SKU-A", 5));


        assertThrows(StockInsuficienteException.class, () -> {
            pedidoService.agregarItem(usuarioUuid, dto);
        });
        

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void comprarArticuloDirecto_DebeCrearPedidoPagado_Y_ReducirStock() {
    
        String usuarioUuid = "user-1";
        AgregarItemDto dto = new AgregarItemDto("SKU-B", 2);

       
        when(inventarioClient.obtenerStock("SKU-B"))
            .thenReturn(new InventarioExternoDto("SKU-B", 10));
        
       
        when(catalogoClient.getProductoBySku("SKU-B"))
            .thenReturn(new ProductoExternoDto("SKU-B", "Prod B", "Desc", new BigDecimal("100.00"), false, "url"));

        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> {
            Pedido p = i.getArgument(0);
            p.setId(1L);
            return p;
        });

        PedidoResponseDto resultado = pedidoService.comprarArticuloDirecto(usuarioUuid, dto);

        assertEquals(Estado.PAGADO, resultado.estado()); 
        assertEquals(new BigDecimal("200.00"), resultado.montoTotal());
        
        verify(inventarioClient).reducirStock("SKU-B", 2);
    }
}