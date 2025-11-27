package pedido.pedidos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AgregarItemDto (

    @NotBlank
    @Schema(description = "Sku del producto", example = "PAR-500-CAJ")
    String sku,

    @NotNull
    @Min(1)
    @Schema(description = "Cantidad del producto", example = "3")
    Integer cantidad
){}
