package pokemon.splender.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CustomResponse {

    public static ResponseEntity<?> ok() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
