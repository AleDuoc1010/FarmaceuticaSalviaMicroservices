package usuario.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginDto(

    @Schema(description = "Email del usuario en el login" , example = "pepe@gmail.com")
    String email,

    @Schema(description = "Contraseña del usuario en el login" , example = "oi812hda")
    String password
) {} 
