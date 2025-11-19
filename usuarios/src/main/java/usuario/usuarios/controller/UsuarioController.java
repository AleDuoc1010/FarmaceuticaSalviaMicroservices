package usuario.usuarios.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import usuario.usuarios.dto.LoginDto;
import usuario.usuarios.dto.LoginResponseDto;
import usuario.usuarios.dto.RegistroDto;
import usuario.usuarios.dto.UsuarioResponseDto;
import usuario.usuarios.model.Usuario;
import usuario.usuarios.service.UsuarioService;
import usuario.usuarios.security.JwtTokenProvider;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuario", description = "Operaciones relacionadas con usuarios")
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    private final JwtTokenProvider jwtTokenProvider;

    public UsuarioController(UsuarioService usuarioService, JwtTokenProvider jwtTokenProvider) {
        this.usuarioService = usuarioService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Operation(summary = "Registrar un nuevo usuario")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    @ApiResponse(responseCode = "409", description = "Email ya registrado")
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDto> registrarUsuario(@Valid @RequestBody RegistroDto registroDto){

        Usuario nuevoUsuario = usuarioService.registrarUsuario(registroDto);

        UsuarioResponseDto responseDto = mapToUsuarioResponseDto(nuevoUsuario);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Iniciar sesión")
    @ApiResponse(responseCode = "200", description = "Login exitoso")
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUsuario(@Valid @RequestBody LoginDto loginDto){

        Usuario usuario = usuarioService.loginUsuario(loginDto);

        String token = jwtTokenProvider.generateToken(usuario);

        UsuarioResponseDto usuarioDto = mapToUsuarioResponseDto(usuario);
        LoginResponseDto loginResponse = new LoginResponseDto(token, usuarioDto);

        return ResponseEntity.ok(loginResponse);
    }

    @Operation(summary = "Obtener usuario por UUID")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @GetMapping("/{uuid}")
    public ResponseEntity<UsuarioResponseDto> getUsuarioByUuid(@PathVariable String uuid){

        Usuario usuario = usuarioService.findByUuid(uuid);
        return ResponseEntity.ok(mapToUsuarioResponseDto(usuario));
    }

    @Operation(summary = "Obtener todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios")
    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDto>> getAllUsuarios(@ParameterObject Pageable pageable){

        Page<Usuario> paginaUsuarios = usuarioService.findAll(pageable);

        Page<UsuarioResponseDto> paginaDto = paginaUsuarios.map(this::mapToUsuarioResponseDto);
        return ResponseEntity.ok(paginaDto);
    }

    @Operation(summary = "Eliminar un usuario por UUID")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String uuid){

        usuarioService.eliminarUsuarioPorUuid(uuid);
        return ResponseEntity.noContent().build();
    }

    private UsuarioResponseDto mapToUsuarioResponseDto(Usuario usuario) {
        return new UsuarioResponseDto(
            usuario.getUuid(),
            usuario.getNombre(),
            usuario.getEmail(),
            usuario.getPhone(),
            usuario.getRol()
        );
    }
    
}
