package usuario.usuarios.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
@Schema(description = "Entidad que representa un usuario en el sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Identificador único del usuario", example = "1")
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    @Schema(description = "UUID público del usuario", example = "550e8400-e29")
    private String uuid;

    @Column(name = "nombre", nullable = false)
    @Schema(description = "Nombre del usuario", example = "Juan Pérez")
    private String nombre;

    @Column(name = "email", nullable = false, unique = true)
    @Schema(description = "Correo electrónico del usuario", example = "")
    private String email;

    @Column(name = "phone", nullable = true)
    @Schema(description = "Número de teléfono del usuario", example = "+34123456789")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    @Schema(description = "Rol del usuario en el sistema", example = "USUARIO")
    private Rol rol;

    @Column(name = "password_hash", nullable = false)
    @Schema(description = "Hash de la contraseña del usuario", example = "5f4dcc3b5aa765d61d8327deb882cf99")
    private String passwordHash;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @Schema(description = "Fecha de creación del usuario")
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    @Schema(description = "Fecha de la última actualización del usuario")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    public void prePersist() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }

}
