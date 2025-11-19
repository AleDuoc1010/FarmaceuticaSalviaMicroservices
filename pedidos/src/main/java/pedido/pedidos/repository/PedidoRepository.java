package pedido.pedidos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pedido.pedidos.model.Estado;
import pedido.pedidos.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Optional<Pedido> findByUsuarioUuidAndEstado(String usuarioUuid, Estado estado);

    List<Pedido> findAllByUsuarioUuidAndEstado(String usuarioUuid, Estado estado);

    Optional<Pedido> findByUuid(String uuid);

    
}
