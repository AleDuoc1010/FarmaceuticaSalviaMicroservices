package producto.productos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import producto.productos.dto.ProductoCreateDto;
import producto.productos.dto.ProductoResponseDto;
import producto.productos.exception.ProductoNotFoundException;
import producto.productos.exception.SkuAlreadyExistsException;
import producto.productos.model.Producto;
import producto.productos.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    private ProductoResponseDto mapToResponseDto(Producto producto){
        return new ProductoResponseDto(
            producto.getId(),
            producto.getSku(),
            producto.getNombre(),
            producto.getDescripcion(),
            producto.getPrecio(),
            producto.getImagenUrl(),
            producto.isDestacado(),
            producto.getFechaCreacion()
        );
    }

    @Transactional
    public ProductoResponseDto crearProducto(ProductoCreateDto createDto){

        if(productoRepository.existsBySku(createDto.sku())){
            throw new SkuAlreadyExistsException("El SKU ya existe: " + createDto.sku());
        }

        Producto nuevoProducto = new Producto();
        nuevoProducto.setSku(createDto.sku());
        nuevoProducto.setNombre(createDto.nombre());
        nuevoProducto.setDescripcion(createDto.descripcion());
        nuevoProducto.setPrecio(createDto.precio());
        nuevoProducto.setImagenUrl(createDto.imagenUrl());
        nuevoProducto.setDestacado(createDto.destacado());

        Producto productoGuardado = productoRepository.save(nuevoProducto);
        return mapToResponseDto(productoGuardado);
    }

    @Transactional(readOnly = true)
    public ProductoResponseDto findBySku(String sku){
        Producto producto = productoRepository.findBySku(sku)
            .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con SKU: " + sku));
        return mapToResponseDto(producto);
    }

    @Transactional(readOnly = true)
    public Page<ProductoResponseDto> findAll(Pageable pageable){
        Page<Producto> paginaProductos = productoRepository.findAll(pageable);
        return paginaProductos.map(this::mapToResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<ProductoResponseDto> findByNombre(String nombre, Pageable pageable){
        Page<Producto> paginaProductos = productoRepository.findByNombreContainingIgnoreCase(nombre, pageable);
        return paginaProductos.map(this::mapToResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<ProductoResponseDto> findDestacados(Pageable pageable){
        Page<Producto> paginaProductos = productoRepository.findByDestacado(true, pageable);
        return paginaProductos.map(this::mapToResponseDto);
    }

    @Transactional
    public void deleteBySku(String sku){
        Producto producto = productoRepository.findBySku(sku)
            .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con SKU: " + sku));
        productoRepository.delete(producto);
    }
    
}
