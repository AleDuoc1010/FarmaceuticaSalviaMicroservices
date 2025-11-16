package usuario.usuarios.dto;

import usuario.usuarios.model.Rol;

public record UsuarioResponseDto(
    String uuid,
    String nombre,
    String email,
    String phone,
    Rol rol
) {}
