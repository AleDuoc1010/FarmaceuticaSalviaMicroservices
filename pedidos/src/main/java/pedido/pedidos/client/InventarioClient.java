package pedido.pedidos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pedido.pedidos.dto.externo.InventarioExternoDto;

@FeignClient(name = "inventario", url = "http://localhost:8083/inventario")
public interface InventarioClient {

    @GetMapping("/{sku}")
    InventarioExternoDto obtenerStock(@PathVariable String sku);

    @PutMapping("/{sku}/reducir")
    InventarioExternoDto reducirStock(@PathVariable String sku, @RequestParam Integer cantidad);
    
}
