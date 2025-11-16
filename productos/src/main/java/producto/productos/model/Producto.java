package producto.productos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productos")
@Schema(description = "Entidad que representa un producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del producto", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Nombre del producto", example = "Paracetamol")
    private String nombre;

    @Column(nullable = false)
    @Schema(description = "Descripción del producto", example = "Analgésico y antipirético")
    private String descripcion;

    @Column(nullable = false)
    @Schema(description = "Precio del producto", example = "5.99")
    private int precio;

    @Column(nullable = false)
    @Schema(description = "Stock disponible del producto", example = "100")
    private int stock;

    @Column(nullable = false)
    @Schema(description = "Imagen del producto", example = "http://example.com/imagen.jpg")
    private String imagen;
    
}
