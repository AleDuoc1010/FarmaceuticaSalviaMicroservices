package usuario.usuarios.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import usuario.usuarios.dto.RegistroDto;
import usuario.usuarios.exception.EmailYaExisteException;
import usuario.usuarios.model.Usuario;
import usuario.usuarios.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void registrarUsuario_DebeGuardarUsuario_CuandoEmailNoExiste() {

        RegistroDto dto = new RegistroDto("Pepito", "pepito@mail.com", "123456", "pass123");
        
        when(usuarioRepository.existsByEmail(dto.email())).thenReturn(false);

        when(passwordEncoder.encode(dto.password())).thenReturn("HASH_SECRETO");

        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

        Usuario resultado = usuarioService.registrarUsuario(dto);

        assertNotNull(resultado);
        assertEquals("HASH_SECRETO", resultado.getPasswordHash());
        assertEquals("pepito@mail.com", resultado.getEmail());
        
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_DebeLanzarExcepcion_CuandoEmailExiste() {

        RegistroDto dto = new RegistroDto("Pepito", "pepito@mail.com", "123", "pass");
        
        when(usuarioRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(EmailYaExisteException.class, () -> {
            usuarioService.registrarUsuario(dto);
        });

        verify(usuarioRepository, never()).save(any());
    }
}