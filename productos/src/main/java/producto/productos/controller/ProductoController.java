package producto.productos.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import producto.productos.dto.ProductoCreateDto;
import producto.productos.dto.ProductoResponseDto;
import producto.productos.service.ProductoService;

@RestController
@RequestMapping("/productos")
@Tag(name = "Producto", description = "Operaciones relacionadas con productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }


    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto en el sistema")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
    @ApiResponse(responseCode = "409", description = "El SKU ya existe")
    @PostMapping
    public ResponseEntity<ProductoResponseDto> crearProducto(@Valid @RequestBody ProductoCreateDto createDto){
        ProductoResponseDto nuevoProducto = productoService.crearProducto(createDto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener producto por SKU", description = "Obtiene los detalles de un producto utilizando su SKU")
    @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @GetMapping("/{sku}")
    public ResponseEntity<ProductoResponseDto> getProductoBySku(@PathVariable String sku){
        ProductoResponseDto producto = productoService.findBySku(sku);
        return ResponseEntity.ok(producto);
    }

    @Operation(summary = "Obtener todos los productos", description = "Obtiene una lista paginada de todos los productos")
    @ApiResponse(responseCode = "200", description = "Productos obtenidos exitosamente")
    @GetMapping
    public ResponseEntity<Page<ProductoResponseDto>> getAllProductos(@ParameterObject Pageable pageable){
        Page<ProductoResponseDto> pagina = productoService.findAll(pageable);
        return ResponseEntity.ok(pagina);
    }

    @Operation(summary = "Buscar productos por nombre", description = "Busca productos cuyo nombre contenga el texto especificado")
    @ApiResponse(responseCode = "200", description = "Productos encontrados exitosamente")
    @GetMapping("/nombre")
    public ResponseEntity<Page<ProductoResponseDto>> buscarPorNombre(@RequestParam String nombre, Pageable pageable){
        Page<ProductoResponseDto> pagina = productoService.findByNombre(nombre, pageable);
        return ResponseEntity.ok(pagina);
    }

    @Operation(summary = "Obtener productos destacados", description = "Obtiene una lista paginada de productos destacados")
    @ApiResponse(responseCode = "200", description = "Productos destacados obtenidos exitosamente")
    @GetMapping("/destacados")
    public ResponseEntity<Page<ProductoResponseDto>> getProductosDestacados(@ParameterObject Pageable pageable){
        Page<ProductoResponseDto> pagina = productoService.findDestacados(pageable);
        return ResponseEntity.ok(pagina);
    }

    @Operation(summary = "Eliminar producto por SKU", description = "Elimina un producto utilizando su SKU")
    @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String sku){
        productoService.deleteBySku(sku);;
        return ResponseEntity.noContent().build();
    }
    

    
}
