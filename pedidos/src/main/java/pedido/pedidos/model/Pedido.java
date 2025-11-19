package pedido.pedidos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "pedidos")
@Schema(description = "Entidad que representa un pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del pedido", example = "1")
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    @Schema(description = "UUID público del pedido", example = "550e8400-e29")
    private String uuid;

    @Column(name = "usuario_uuid", nullable = false)
    @Schema(description = "Identificador del cliente que realizó el pedido", example = "123e4567-e89b-12d3-a456-426614174000")
    private String usuarioUuid;

    @Column(name = "monto_total" ,nullable = false)
    @Schema(description = "Monto total del pedido", example = "25000")
    private BigDecimal montoTotal = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Estado del pedido", example = "PENDIENTE")
    private Estado estado = Estado.PENDIENTE;

    @CreationTimestamp
    private LocalDateTime fecha_pedido;  
    
    @UpdateTimestamp
    private LocalDateTime fecha_actualizacion;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de items asociados al pedido")
    private List<ItemsPedido> items = new ArrayList<>();

    @PrePersist
    public void PrePersist() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }
}
