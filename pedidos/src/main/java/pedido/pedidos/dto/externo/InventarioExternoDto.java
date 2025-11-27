package pedido.pedidos.dto.externo;

import io.swagger.v3.oas.annotations.media.Schema;

public record InventarioExternoDto(

    @Schema(description = "Sku del producto", example = "PAR-500-CAJ")
    String sku,

    @Schema(description = "Cantidad del producto", example = "3")
    Integer cantidad
) {}
