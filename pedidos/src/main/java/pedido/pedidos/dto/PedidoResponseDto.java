package pedido.pedidos.dto;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import pedido.pedidos.model.Estado;

public record PedidoResponseDto(

    @Schema(description = "ID del pedido", example = "1")
    Long id,

    @Schema(description = "UUID público del pedido", example = "apiogjqw9ifi39f")
    String uuid,

    @Schema(description = "Monto total del pedido", example = "54990")
    BigDecimal montoTotal,

    @Schema(description = "Estado del pedido", example = "PENDIENTE")
    Estado estado,

    @Schema(description = "lista de los items en pedido")
    List<ItemPedidoDto> items
) {

    public record ItemPedidoDto(

        @Schema(description = "Id del producto en pedido", example = "1")
        Long id,

        @Schema(description = "SKU único del producto en pedido", example = "PAR-500-CAJ")
        String sku,

        @Schema(description = "Cantidad del producto en pedido", example = "3")
        Integer cantidad,

        @Schema(description = "Precio unitario del producto en pedido", example = "5490")
        BigDecimal precioUnitario,

        @Schema(description = "Subtotal del total de productos en pedido", example = "10990")
        BigDecimal subtotal
    ) {}
}