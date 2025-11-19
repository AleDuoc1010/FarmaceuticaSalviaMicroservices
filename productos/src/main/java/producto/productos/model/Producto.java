package producto.productos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "productos")
@Schema(description = "Entidad que representa un producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del producto", example = "1")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    @Schema(description = "Identificador público, Stock keeping Unit (SKU)", example = "PRD001")
    private String sku;

    @Column(nullable = false)
    @Schema(description = "Nombre del producto", example = "Paracetamol")
    private String nombre;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Descripción del producto", example = "Analgésico y antipirético")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "Precio del producto", example = "12500")
    private BigDecimal precio;

    @Column(name = "imagen_url", length = 1024)
    @Schema(description = "URL de la Imagen del producto", example = "http://example.com/imagen.jpg")
    private String imagenUrl;

    @Column(name = "pide_receta", nullable = false)
    @Schema(description = "Indica si el producto requiere receta médica")
    private boolean pideReceta = false;

    @Column(nullable = false)
    @Schema(description = "Indica si el producto es destacado", example = "true")
    private boolean destacado = false;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false, nullable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
}
