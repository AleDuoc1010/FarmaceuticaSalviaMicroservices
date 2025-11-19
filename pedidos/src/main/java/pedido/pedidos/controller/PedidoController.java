package pedido.pedidos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import pedido.pedidos.dto.AgregarItemDto;
import pedido.pedidos.dto.PedidoResponseDto;
import pedido.pedidos.service.PedidoService;



@RestController
@RequestMapping("/pedidos")
@Tag(name= "Pedidos", description = "Gestión de Carrito y Compras")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService){
        this.pedidoService = pedidoService;
    }

    @PostMapping("/carrito")
    @Operation(summary = "Agregar item al carrito")
    public ResponseEntity<PedidoResponseDto> agregarItem(
        @Valid @RequestBody AgregarItemDto dto,
        Authentication authentication
    ){
        String usuarioUuid = (String) authentication.getPrincipal();
        return ResponseEntity.ok(pedidoService.agregarItem(usuarioUuid, dto));
    }

    @GetMapping("/carrito")
    @Operation(summary = "Ver mi carrito actual")
    public ResponseEntity<PedidoResponseDto> verCarrito(Authentication authentication){
        String usuarioUuid = (String) authentication.getPrincipal();
        PedidoResponseDto carrito = pedidoService.obtenerCarrito(usuarioUuid);

        if(carrito == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(carrito);
    }

    @GetMapping("/historial")
    @Operation(summary = "Ver Historial de compras")
    public ResponseEntity<List<PedidoResponseDto>> verHistorial(Authentication authentication){
        String usuarioUuid = (String) authentication.getPrincipal();
        return ResponseEntity.ok(pedidoService.obtenerHistorial(usuarioUuid));
    }

    @PostMapping("/carrito/pagar")
    @Operation(summary = "Pagar y finalizar compra")
    public ResponseEntity<PedidoResponseDto> pagarCarrito(Authentication authentication) {
        String usuarioUuid = (String) authentication.getPrincipal();
        return ResponseEntity.ok(pedidoService.pagarCarrito(usuarioUuid));
    }

    @PostMapping("/comprar")
    @Operation(summary = "Comprar un producto directamente")
    public ResponseEntity<PedidoResponseDto> comprarProducto(
        @Valid @RequestBody AgregarItemDto dto,
        Authentication authentication
    ){
        String usuarioUuid = (String) authentication.getPrincipal();
        return ResponseEntity.ok(pedidoService.comprarArticuloDirecto(usuarioUuid, dto));
    }

    @DeleteMapping("/carrito/{sku}")
    @Operation(summary = "Eliminar un producto del carrito")
    public ResponseEntity<Void> eliminarItem(
        @PathVariable String sku,
        Authentication authentication
    ){
        String usuarioUuid = (String) authentication.getPrincipal();
        pedidoService.eliminarItem(usuarioUuid, sku);
        return ResponseEntity.noContent().build();
    }
    
}
