package inventario.inventario.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del inventario", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Identificador público del producto, Stock keeping Unit (SKU)", example = "PRD001")
    private String sku;

    @Column(nullable = false)
    @Schema(description = "Cantidad disponible en el inventario", example = "100")
    private Integer cantidad;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}
