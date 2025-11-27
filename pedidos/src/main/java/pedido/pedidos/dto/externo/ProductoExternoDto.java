package pedido.pedidos.dto.externo;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductoExternoDto(

    @Schema(description = "Sku del producto", example = "PAR-500-CAJ")
    String sku,

    @Schema(description = "Nombre del producto", example = "Paracetamol")
    String nombre,

    @Schema(description = "Descripción del producto", example = "Caja con 20 tabletas de Paracetamol 500mg")
    String descripcion,

    @Schema(description = "Precio del producto", example = "2500")
    BigDecimal precio,

    @Schema(description = "Indica si requiere receta médica")
    boolean pideReceta,

    @Schema(description = "URL de la imagen del producto", example = "http://example.com/images/paracetamol.jpg")
    String imagenUrl
) {}
