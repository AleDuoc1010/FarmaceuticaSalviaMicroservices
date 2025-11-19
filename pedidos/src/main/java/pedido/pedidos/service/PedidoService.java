package pedido.pedidos.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pedido.pedidos.client.CatalogoClient;
import pedido.pedidos.client.InventarioClient;
import pedido.pedidos.dto.AgregarItemDto;
import pedido.pedidos.dto.PedidoResponseDto;
import pedido.pedidos.dto.externo.InventarioExternoDto;
import pedido.pedidos.dto.externo.ProductoExternoDto;
import pedido.pedidos.exception.CarritoVacioException;
import pedido.pedidos.exception.PedidoNotFoundException;
import pedido.pedidos.exception.StockInsuficienteException;
import pedido.pedidos.model.Estado;
import pedido.pedidos.model.ItemsPedido;
import pedido.pedidos.model.Pedido;
import pedido.pedidos.repository.PedidoRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CatalogoClient catalogoClient;
    private final InventarioClient inventarioClient;

    public PedidoService(PedidoRepository pedidoRepository, CatalogoClient catalogoClient,
            InventarioClient inventarioClient) {
        this.pedidoRepository = pedidoRepository;
        this.catalogoClient = catalogoClient;
        this.inventarioClient = inventarioClient;
    }

    @Transactional
    public PedidoResponseDto agregarItem(String usuarioUuid, AgregarItemDto dto){

        Pedido pedido = pedidoRepository.findByUsuarioUuidAndEstado(usuarioUuid, Estado.PENDIENTE)
        .orElseGet(() -> {
            Pedido nuevo = new Pedido();
            nuevo.setUsuarioUuid(usuarioUuid);
            nuevo.setEstado(Estado.PENDIENTE);
            return nuevo;
        });

        ProductoExternoDto productoInfo = catalogoClient.getProductoBySku(dto.sku());

        InventarioExternoDto stockInfo = inventarioClient.obtenerStock(dto.sku());
        if(stockInfo.cantidad() < dto.cantidad()){
            throw new StockInsuficienteException("Stock insuficiente. Disponible: " + stockInfo.cantidad());
        }

        Optional<ItemsPedido> itemExistente = pedido.getItems().stream()
        .filter(i -> i.getSkuProducto().equals(dto.sku()))
        .findFirst();

        if (itemExistente.isPresent()){
            ItemsPedido item = itemExistente.get();
            item.setCantidad(item.getCantidad() + dto.cantidad());
        }else{
            ItemsPedido nuevoItem = new ItemsPedido();
            nuevoItem.setPedido(pedido);
            nuevoItem.setSkuProducto(dto.sku());
            nuevoItem.setCantidad(dto.cantidad());
            nuevoItem.setPrecioUnitario(productoInfo.precio());
            
            pedido.getItems().add(nuevoItem);
        }
        calcularTotal(pedido);
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        return mapToDto(pedidoGuardado);
    }

    @Transactional(readOnly = true)
    public PedidoResponseDto obtenerCarrito(String usuarioUuid){
        return pedidoRepository.findByUsuarioUuidAndEstado(usuarioUuid, Estado.PENDIENTE)
        .map(this::mapToDto)
        .orElse(null);
    }

    @Transactional
    public PedidoResponseDto pagarCarrito(String usuarioUuid){

        Pedido pedido = pedidoRepository.findByUsuarioUuidAndEstado(usuarioUuid, Estado.PENDIENTE)
        .orElseThrow(() -> new PedidoNotFoundException("No hay carrito para pagar"));

        if(pedido.getItems().isEmpty()){
            throw new CarritoVacioException("El carrito está vacío");
        }

        for (ItemsPedido item : pedido.getItems()){
            inventarioClient.reducirStock(item.getSkuProducto(), item.getCantidad());
        }

        pedido.setEstado(Estado.PAGADO);
        Pedido pedidoPagado = pedidoRepository.save(pedido);

        return mapToDto(pedidoPagado);
    }

    @Transactional
    public void eliminarItem(String usuarioUuid, String sku){
        Pedido pedido = pedidoRepository.findByUsuarioUuidAndEstado(usuarioUuid, Estado.PENDIENTE)
        .orElseThrow(() -> new PedidoNotFoundException("No hay carrito activo"));

        boolean eliminado = pedido.getItems().removeIf(i -> i.getSkuProducto().equals(sku));

        if (eliminado) {
            calcularTotal(pedido);
            pedidoRepository.save(pedido);
        }
    }

    private void calcularTotal(Pedido pedido){
        BigDecimal total = BigDecimal.ZERO;
        for (ItemsPedido item : pedido.getItems()){
            BigDecimal subtotal = item.getPrecioUnitario()
            .multiply(BigDecimal.valueOf(item.getCantidad()));

            total = total.add(subtotal);
        }
        pedido.setMontoTotal(total);
    }

    private PedidoResponseDto mapToDto(Pedido pedido){
        List<PedidoResponseDto.ItemPedidoDto> itemsDto = pedido.getItems().stream()
        .map(i -> new PedidoResponseDto.ItemPedidoDto(
            i.getId(),
            i.getSkuProducto(),
            i.getCantidad(),
            i.getPrecioUnitario(),
            i.getPrecioUnitario().multiply(BigDecimal.valueOf(i.getCantidad()))
        )).collect(Collectors.toList());

        return new PedidoResponseDto(
            pedido.getId(),
            pedido.getUuid(),
            pedido.getMontoTotal(),
            pedido.getEstado(),
            itemsDto
        );
    }
    
}
