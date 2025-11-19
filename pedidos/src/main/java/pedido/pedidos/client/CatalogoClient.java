package pedido.pedidos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import pedido.pedidos.dto.externo.ProductoExternoDto;

@FeignClient(name = "catalogo", url = "http://localhost:8082/productos")
public interface CatalogoClient {

    @GetMapping("/{sku}")
    ProductoExternoDto getProductoBySku(@PathVariable String sku);
    
}
