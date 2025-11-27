package usuario.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDto(

    @Schema(description = "Token del usuario" , example = "nijfuje9j2j3dji010oi2e1odijwovvj8934jf98")
    String token,

    @Schema(description = "Datos del usuario")
    UsuarioResponseDto usuario
) {}
