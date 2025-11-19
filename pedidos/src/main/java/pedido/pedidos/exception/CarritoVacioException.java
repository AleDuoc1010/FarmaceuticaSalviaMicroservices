package pedido.pedidos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CarritoVacioException extends RuntimeException {
    public CarritoVacioException(String message){
        super(message);
    }
    
}
