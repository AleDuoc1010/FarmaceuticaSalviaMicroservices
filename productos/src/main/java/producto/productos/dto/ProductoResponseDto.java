package producto.productos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductoResponseDto(

    @Schema(description = "Id del producto" , example = "1")
    Long id,

    @Schema(description = "Sku público del producto" , example = "PAR-500")
    String sku,

    @Schema(description = "Nombre del producto" , example = "Paracetamol 500 mg")
    String nombre,

    @Schema(description = "Descripción del producto" , example = "Alivio rapido del dolor")
    String descripcion,

    @Schema(description = "Precio del producto" , example = "12500")
    BigDecimal precio,

    @Schema(description = "URL de la imagen del producto" , example = "/paracetamol.jpg")
    String imagenUrl,

    @Schema(description = "Dato que define si el producto es destacado" , example = "true")
    boolean destacado,

    @Schema(description = "Dato que define si el producto pide receta" , example = "true")
    boolean pideReceta,

    LocalDateTime fechaCreacion

) {}
    