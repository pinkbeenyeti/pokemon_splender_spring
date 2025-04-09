package pokemon.splender.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 값이 null일 때 JSON에서 제외 가능
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private int code;
    private String message;
    private String detail; // null 가능
}
