package pokemon.splender.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static pokemon.splender.exception.ErrorMessage.DUPLICATED_NICKNAME;
import static pokemon.splender.exception.ErrorMessage.INVALID_NAME_CHANGE_PERIOD;
import static pokemon.splender.exception.ErrorMessage.INVALID_NICKNAME_FORMAT;
import static pokemon.splender.exception.ErrorMessage.INVALID_NICKNAME_FORMAT_NOT_EMOTICON;
import static pokemon.splender.exception.ErrorMessage.INVALID_NICKNAME_FORMAT_ONLY_NUMBER;
import static pokemon.splender.exception.ErrorMessage.INVALID_NICKNAME_LENGTH;
import static pokemon.splender.exception.ErrorMessage.NICKNAME_REQUIRED;
import static pokemon.splender.exception.ErrorMessage.USER_NOT_FOUND;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CustomMVCException extends RuntimeException {

    private final ErrorMessage errorMessage;
    private final HttpStatus status;

    public static CustomMVCException nameRequired() {
        return new CustomMVCException(NICKNAME_REQUIRED, BAD_REQUEST);
    }

    public static CustomMVCException invalidNameLength() {
        return new CustomMVCException(INVALID_NICKNAME_LENGTH, BAD_REQUEST);
    }

    public static CustomMVCException invalidNameFormat() {
        return new CustomMVCException(INVALID_NICKNAME_FORMAT, BAD_REQUEST);
    }

    public static CustomMVCException invalidNameFormatOnlyNumber() {
        return new CustomMVCException(INVALID_NICKNAME_FORMAT_ONLY_NUMBER, BAD_REQUEST);
    }

    public static CustomMVCException invalidNameFormatNotEmoticon() {
        return new CustomMVCException(INVALID_NICKNAME_FORMAT_NOT_EMOTICON, BAD_REQUEST);
    }

    public static CustomMVCException duplicatedName() {
        return new CustomMVCException(DUPLICATED_NICKNAME, BAD_REQUEST);
    }

    public static CustomMVCException invalidNameChangePeriod() {
        return new CustomMVCException(INVALID_NAME_CHANGE_PERIOD, BAD_REQUEST);
    }

    public static CustomMVCException userNotFound() {
        return new CustomMVCException(USER_NOT_FOUND, BAD_REQUEST);
    }
}
