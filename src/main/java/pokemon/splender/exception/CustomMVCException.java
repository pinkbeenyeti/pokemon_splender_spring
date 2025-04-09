package pokemon.splender.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static pokemon.splender.exception.ErrorMessage.INVALID_IMAGE;
import static pokemon.splender.exception.ErrorMessage.INVALID_NAME_CHANGE_PERIOD;
import static pokemon.splender.exception.ErrorMessage.NOT_EXIST_IMAGE;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomMVCException extends RuntimeException {

    private final ErrorMessage errorMessage;
    private final HttpStatus status;
    private final String detail; // 명확한 오류 메시지

    // 이미지 관련 제외 나머지는 detail이 필요 없으므로
    // RequiredArgsConstructor 제거 후 직접 생성자 추가
    public CustomMVCException(ErrorMessage errorMessage, HttpStatus status) {
        this.errorMessage = errorMessage;
        this.status = status;
        this.detail = null;
    }

    public CustomMVCException(ErrorMessage errorMessage, HttpStatus status, String detail) {
        this.errorMessage = errorMessage;
        this.status = status;
        this.detail = detail;
    }


    public static CustomMVCException invalidNameChangePeriod() {
        return new CustomMVCException(INVALID_NAME_CHANGE_PERIOD, BAD_REQUEST);
    }

    // 명확한 오류 메시지를 포함하기 위해 String detail 추가
    public static CustomMVCException invalidImage(String detail) {
        return new CustomMVCException(INVALID_IMAGE, BAD_REQUEST, detail);
    }

    public static CustomMVCException notExistImage() {
        return new CustomMVCException(NOT_EXIST_IMAGE, BAD_REQUEST);
    }
}
