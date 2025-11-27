package inventario.inventario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InventarioDto(
    @NotBlank
    @Schema(description = "SKU único del producto", example = "PAR-500-CAJ")
    String sku,

    @NotNull
    @Min(0)
    @Schema(description = "Stock del producto", example = "10")
    Integer cantidad
){}
