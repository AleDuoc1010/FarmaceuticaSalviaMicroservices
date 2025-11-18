package inventario.inventario.controller;

import org.springframework.http.ResponseEntity;
import inventario.inventario.dto.InventarioDto;
import inventario.inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventario")
@Tag(name = "Inventario", description = "Operaciones relacionadas con el inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @Operation(summary = "Ver stock de un producto", description = "Obtiene la cantidad disponible en stock para un producto específico.")
    @GetMapping("/{sku}")
    public ResponseEntity<InventarioDto> obtenerStock(@PathVariable String sku) {
        return ResponseEntity.ok(inventarioService.obtenerStock(sku));
    }

    @Operation(summary = "Crear registro de inventario", description = "Crea un nuevo registro en el inventario.")
    @PostMapping
    public ResponseEntity<InventarioDto> crearRegistro(@Valid @RequestBody InventarioDto dto){
        return new ResponseEntity<>(inventarioService.crearRegistroInventario(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Agregar stock a un producto", description = "Agrega una cantidad específica de stock a un producto existente.")
    @PutMapping("/{sku}/agregar")
    public ResponseEntity<InventarioDto> agregarStock(
            @PathVariable String sku,
            @RequestParam Integer cantidad){
        return ResponseEntity.ok(inventarioService.agregarStock(sku, cantidad));
    }

    @Operation(summary = "Reducir stock de un producto", description = "Reduce una cantidad específica de stock de un producto existente.")
    @PutMapping("/{sku}/reducir")
    public ResponseEntity<InventarioDto> reducirStock(
            @PathVariable String sku,
            @RequestParam Integer cantidad){
        return ResponseEntity.ok(inventarioService.reducirStock(sku, cantidad));
    }
    
}
