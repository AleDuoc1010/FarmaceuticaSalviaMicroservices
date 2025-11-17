package producto.productos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductoResponseDto(

    Long id,
    String sku,
    String nombre,
    String descripcion,
    BigDecimal precio,
    String imagenUrl,
    boolean destacado,
    LocalDateTime fechaCreacion

) {}
    