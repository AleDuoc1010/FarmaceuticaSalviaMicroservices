package usuario.usuarios.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import usuario.usuarios.model.Usuario;
import usuario.usuarios.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        Usuario usuario = usuarioRepository.findByUuid(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con UUID: " + username));

        String role = "ROLE_" + usuario.getRol().name();
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(role));

        return new User(
            usuario.getUuid(),
            usuario.getPasswordHash(),
            authorities
        );
    }
    
}
