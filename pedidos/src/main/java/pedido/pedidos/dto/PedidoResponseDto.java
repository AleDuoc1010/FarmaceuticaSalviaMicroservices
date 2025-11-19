package pedido.pedidos.dto;

import java.math.BigDecimal;
import java.util.List;

import pedido.pedidos.model.Estado;

public record PedidoResponseDto(
    Long id,
    String uuid,
    BigDecimal montoTotal,
    Estado estado,
    List<ItemPedidoDto> items
) {

    public record ItemPedidoDto(
        Long id,
        String sku,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
    ) {}
}