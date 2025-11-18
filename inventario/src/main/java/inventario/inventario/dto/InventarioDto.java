package inventario.inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InventarioDto(
    @NotBlank
    String sku,

    @NotNull
    @Min(0)
    Integer cantidad
){}
