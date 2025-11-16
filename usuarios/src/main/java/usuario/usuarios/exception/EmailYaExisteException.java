package usuario.usuarios.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailYaExisteException extends RuntimeException {
    public EmailYaExisteException(String message) {
        super(message);
    }
}
