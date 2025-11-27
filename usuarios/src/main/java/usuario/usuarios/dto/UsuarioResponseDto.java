package usuario.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import usuario.usuarios.model.Rol;

public record UsuarioResponseDto(

    @Schema(description = "Identificador público del usuario" , example = "8fvn30fjp2lke")
    String uuid,

    @Schema(description = "Nombre del usuario" , example = "Pepito")
    String nombre,

    @Schema(description = "Email del usuario" , example = "pepe@gmail.com")
    String email,

    @Schema(description = "Teléfono del usuario" , example = "123456789")
    String phone,

    @Schema(description = "Rol del usuario" , example = "USUARIO")
    Rol rol
) {}
