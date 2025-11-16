package usuario.usuarios.dto;


public record RegistroDto(
    String nombre,
    String email,
    String phone,
    String password) {}