package usuario.usuarios.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import usuario.usuarios.dto.LoginDto;
import usuario.usuarios.dto.RegistroDto;
import usuario.usuarios.exception.EmailYaExisteException;
import usuario.usuarios.exception.UsuarioNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import usuario.usuarios.model.Rol;
import usuario.usuarios.model.Usuario;
import usuario.usuarios.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<Usuario> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Usuario findByUuid(String uuid) {
        return usuarioRepository.findByUuid(uuid)
        .orElseThrow(() -> new UsuarioNotFoundException("Usuario con UUID " + uuid + " no encontrado"));
    }

    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario registrarUsuario(RegistroDto registroDTO) {
        if (usuarioRepository.existsByEmail(registroDTO.email())) {
            throw new EmailYaExisteException("El correo electrónico ya está en uso" + registroDTO.email());
        }

        String passwordHash = passwordEncoder.encode(registroDTO.password());

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(registroDTO.nombre());
        nuevoUsuario.setEmail(registroDTO.email());
        nuevoUsuario.setPhone(registroDTO.phone());
        nuevoUsuario.setPasswordHash(passwordHash);
        nuevoUsuario.setRol(Rol.USUARIO);

        return usuarioRepository.save(nuevoUsuario);
    }

    @Transactional(readOnly = true)
    public Usuario loginUsuario(LoginDto loginDTO) {

        Usuario usuario = usuarioRepository.findByEmail(loginDTO.email())
                .orElseThrow(() -> new BadCredentialsException("Email o contraseña incorrectos"));

        if (passwordEncoder.matches(loginDTO.password(), usuario.getPasswordHash())) {
            return usuario;
        } else {
            throw new BadCredentialsException("Email o contraseña incorrectos");
        }
    }

    public Usuario cambiarRol(String uuid, String nuevoRolStr) {
        Usuario usuario = usuarioRepository.findByUuid(uuid)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        try {
            Rol rolEnum = Rol.valueOf(nuevoRolStr.toUpperCase());
            usuario.setRol(rolEnum);
        } catch (IllegalArgumentException e){
            throw new RuntimeException("Rol no válido. Use 'ADMINISTRADOR' O 'USUARIO'");
        }

        return usuarioRepository.save(usuario);
   }


    @Transactional
    public void eliminarUsuarioPorUuid(String uuid) {
        usuarioRepository.deleteByUuid(uuid);
    }
    
}
