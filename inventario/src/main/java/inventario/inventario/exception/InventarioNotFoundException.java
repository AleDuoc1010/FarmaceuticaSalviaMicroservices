package inventario.inventario.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InventarioNotFoundException extends RuntimeException {
    public InventarioNotFoundException(String message) {
        super(message);
    }
    
}
