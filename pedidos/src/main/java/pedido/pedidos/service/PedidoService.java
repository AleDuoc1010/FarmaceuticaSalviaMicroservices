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

        Optional<ItemsPedido> itemExistente = pedido.getItems().stream()
        .filter(i -> i.getSkuProducto().equals(dto.sku()))
        .findFirst();

        int cantidadActualEnCarrito = itemExistente.map(ItemsPedido::getCantidad).orElse(0);
        int cantidadTotalDeseada = cantidadActualEnCarrito + dto.cantidad();

        InventarioExternoDto stockInfo = inventarioClient.obtenerStock(dto.sku());
        if(stockInfo.cantidad() < cantidadTotalDeseada){
            throw new StockInsuficienteException("Stock insuficiente.");
        }

        if (itemExistente.isPresent()){
            ItemsPedido item = itemExistente.get();
            item.setCantidad(cantidadTotalDeseada);
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

    @Transactional
    public PedidoResponseDto modificarCantidad(String usuarioUuid, String sku, Integer nuevaCantidad){
        Pedido pedido = pedidoRepository.findByUsuarioUuidAndEstado(usuarioUuid, Estado.PENDIENTE)
        .orElseThrow(() -> new PedidoNotFoundException("No hay carrito activo"));

        if (nuevaCantidad <= 0) {
            eliminarItem(usuarioUuid, sku);
            return obtenerCarrito(usuarioUuid);
        }

        InventarioExternoDto stockInfo = inventarioClient.obtenerStock(sku);
        if (stockInfo.cantidad() < nuevaCantidad){
            throw new StockInsuficienteException("Stock insuficiente. Solo hay "+ stockInfo.cantidad() + " disponibles.");
        }

        ItemsPedido item = pedido.getItems().stream()
            .filter(i -> i.getSkuProducto(). equals(sku))
            .findFirst()
            .orElseThrow(() -> new PedidoNotFoundException("El producto no está en el carrito"));

        item.setCantidad(nuevaCantidad);

        calcularTotal(pedido);
        return mapToDto(pedidoRepository.save(pedido));
    }

    @Transactional
    public void vaciarCarrito(String usuarioUuid) {
        Pedido pedido = pedidoRepository.findByUsuarioUuidAndEstado(usuarioUuid, Estado.PENDIENTE)
                .orElseThrow(() -> new PedidoNotFoundException("No hay carrito activo"));
        
        pedido.getItems().clear();
        pedido.setMontoTotal(BigDecimal.ZERO);
        pedidoRepository.save(pedido);
    }

    @Transactional(readOnly = true)
    public PedidoResponseDto obtenerCarrito(String usuarioUuid){
        return pedidoRepository.findByUsuarioUuidAndEstado(usuarioUuid, Estado.PENDIENTE)
        .map(this::mapToDto)
        .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDto> obtenerHistorial(String usuarioUuid){
        List<Pedido> pedidosPagados = pedidoRepository.findAllByUsuarioUuidAndEstado(usuarioUuid, Estado.PAGADO);

        return pedidosPagados.stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
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
    public PedidoResponseDto comprarArticuloDirecto(String usuarioUuid, AgregarItemDto dto) {

        InventarioExternoDto stockInfo = inventarioClient.obtenerStock(dto.sku());
        if(stockInfo.cantidad() < dto.cantidad()){
            throw new StockInsuficienteException("Stock insuficiente. Disponible: " + stockInfo.cantidad());
        }

        ProductoExternoDto productoInfo = catalogoClient.getProductoBySku(dto.sku());

        Pedido pedido = new Pedido();
        pedido.setUsuarioUuid(usuarioUuid);
        pedido.setEstado(Estado.PAGADO);

        ItemsPedido item = new ItemsPedido();
        item.setPedido(pedido);
        item.setSkuProducto(dto.sku());
        item.setCantidad(dto.cantidad());
        item.setPrecioUnitario(productoInfo.precio());

        pedido.getItems().add(item);

        calcularTotal(pedido);

        inventarioClient.reducirStock(dto.sku(), dto.cantidad());

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        return mapToDto(pedidoGuardado);
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

    @Transactional
    public void eliminarPedidoHistorial(String usuarioUuid, Long pedidoId) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado"));

        if (!pedido.getUsuarioUuid().equals(usuarioUuid)) {
            throw new RuntimeException("No tienes permiso para eliminar este pedido");
        }

        if (pedido.getEstado() != Estado.PAGADO) {
            throw new RuntimeException("Solo se pueden eliminar pedidos del historial (Pagados)");
        }

        pedidoRepository.delete(pedido);
    }

    @Transactional
    public void borrarHistorialCompleto(String usuarioUuid) {
        List<Pedido> historial = pedidoRepository.findAllByUsuarioUuidAndEstado(usuarioUuid, Estado.PAGADO);
        pedidoRepository.deleteAll(historial);
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
