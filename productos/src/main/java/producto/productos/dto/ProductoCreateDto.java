package producto.productos.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductoCreateDto(
    @NotBlank @Size(max = 100)
    @Schema(description = "SKU único del producto", example = "PAR-500-CAJ")
    String sku,

    @NotBlank @Size(max = 255)
    @Schema(description = "Nombre del producto", example = "Paracetamol 500mg Caja")
    String nombre,

    @Schema(description = "Descripción del producto", example = "Caja con 20 tabletas de Paracetamol 500mg")
    String descripcion,

    @NotNull @Positive
    @Schema(description = "Precio del producto", example = "2500")
    BigDecimal precio,

    @NotNull
    @Schema(description = "Indica si el producto es destacado", example = "true")
    boolean destacado,

    @NotNull
    @Schema(description = "Indica si requiere receta médica")
    Boolean pideReceta,

    @Schema(description = "URL de la imagen del producto", example = "http://example.com/images/paracetamol.jpg")
    String imagenUrl
){}
