package producto.productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public class ProductoRepository {

    @Query(value = "SELECT * FROM Producto WHERE LOWER(nombre) = LOWER(:nombre)", nativeQuery = true)
    List<Producto> findByNombre(@Param("nombre") String nombre);
    
}
