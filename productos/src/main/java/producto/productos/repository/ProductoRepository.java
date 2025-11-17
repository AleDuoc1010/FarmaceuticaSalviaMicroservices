package producto.productos.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;

import producto.productos.model.Producto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    Optional<Producto> findBySku(String sku);

    boolean existsBySku(String sku);

    Page<Producto> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    Page<Producto> findByDestacado(boolean destacado, Pageable pageable);
}
