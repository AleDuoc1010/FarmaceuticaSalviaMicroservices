package usuario.usuarios.repository;

import java.util.Optional;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import usuario.usuarios.model.Rol;
import usuario.usuarios.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    

    Optional<Usuario> findByUuid(String uuid);

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<Usuario> findByRol(Rol rol, Pageable pageable);

    Page<Usuario> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    @Transactional
    @Modifying
    long deleteByUuid(String uuid);
}
