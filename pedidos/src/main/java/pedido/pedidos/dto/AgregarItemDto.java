package pedido.pedidos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AgregarItemDto (

    @NotBlank
    String sku,

    @NotNull
    @Min(1)
    Integer cantidad
){}
