package pedido.pedidos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "items_pedido")
@Schema(description = "Entidad que representa un ítem dentro de un pedido")
public class ItemsPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id de productos en pedido", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    @Schema(description = "Pedido al que pertenece el ítem")
    private Pedido pedido;

    @Column(name = "sku_producto", nullable = false)
    @Schema(description = "SKU del producto", example = "PRD001")
    private String skuProducto;

    @Column(nullable = false)
    @Schema(description = "Cantidad del producto en el ítem del pedido", example = "2")
    private Integer cantidad;

    @Column(name = "precio_unitario_congelado", nullable = false)
    @Schema(description = "Precio unitario del producto en el ítem del pedido", example = "12500")
    private BigDecimal precioUnitario;
    
}
