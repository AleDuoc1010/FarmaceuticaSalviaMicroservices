package usuario.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegistroDto(

    @Schema(description = "Nombre del usuario" , example = "Pepito")
    String nombre,

    @Schema(description = "Email del usuario" , example = "pepe@gmail.com")
    String email,

    @Schema(description = "Teléfono del usuario" , example = "123456789")
    String phone,

    @Schema(description = "Contraseña del usuario" , example = "oi812hda")
    String password
) {}