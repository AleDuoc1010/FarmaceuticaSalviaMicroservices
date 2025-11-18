package inventario.inventario.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import inventario.inventario.dto.InventarioDto;
import inventario.inventario.exception.InventarioNotFoundException;
import inventario.inventario.exception.StockInsuficienteException;
import inventario.inventario.model.Inventario;
import inventario.inventario.repository.InventarioRepository;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;  
    
    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Transactional(readOnly = true)
    public InventarioDto obtenerStock(String sku){
        Inventario inventario = inventarioRepository.findBySku(sku)
        .orElseThrow(() -> new InventarioNotFoundException("No existe inventario para SKU: " + sku));

        return new InventarioDto(inventario.getSku(), inventario.getCantidad());
    }

    @Transactional(readOnly = true)
    public boolean hayStockDisponible(String sku, Integer cantidadRequerida){
        return inventarioRepository.findBySku(sku)
        .map(inv -> inv.getCantidad() >= cantidadRequerida)
        .orElse(false);
    }

    @Transactional
    public InventarioDto crearRegistroInventario(InventarioDto dto){
        if(inventarioRepository.existsBySku(dto.sku())){
            throw new RuntimeException("El inventario para este SKU ya existe");
        }

        Inventario nuevo = new Inventario();
        nuevo.setSku(dto.sku());
        nuevo.setCantidad(dto.cantidad());

        inventarioRepository.save(nuevo);
        return new InventarioDto(nuevo.getSku(), nuevo.getCantidad());
    }

    @Transactional
    public InventarioDto agregarStock(String sku, Integer cantidad) {
        Inventario inventario = inventarioRepository.findBySku(sku)
        .orElseThrow(() -> new InventarioNotFoundException("Ya existe inventario para SKU: " + sku));

        inventario.setCantidad(inventario.getCantidad() + cantidad);

        inventarioRepository.save(inventario);
        return new InventarioDto(inventario.getSku(), inventario.getCantidad());
    }

    @Transactional
    public InventarioDto reducirStock(String sku, Integer cantidad){
        Inventario inventario = inventarioRepository.findBySku(sku)
        .orElseThrow(() -> new InventarioNotFoundException("No existe inventario para SKU: " + sku));

        if(inventario.getCantidad() < cantidad){
            throw new StockInsuficienteException("Stock insuficiente para SKU: " + sku + ". Disponible: " + inventario.getCantidad());
        }

        inventario.setCantidad(inventario.getCantidad() - cantidad);

        inventarioRepository.save(inventario);
        return new InventarioDto(inventario.getSku(), inventario.getCantidad());
    }
    
}
