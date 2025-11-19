package pedido.pedidos.dto.externo;

import java.math.BigDecimal;

public record ProductoExternoDto(
    String sku,
    String nombre,
    String descripcion,
    BigDecimal precio,
    String imagenUrl
) {}
