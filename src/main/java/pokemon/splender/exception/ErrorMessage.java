package pokemon.splender.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorMessage {

    //request 관련 문제
    EMPTY_COOKIES(-10000, "쿠키가 비어있습니다."),
    NOT_EXIST_ACCESS_COOKIE(-10001, "엑세스 토큰 쿠키가 존재하지 않습니다."),
    NOT_EXIST_REFRESH_COOKIE(-10002, "리프레시 토큰 쿠키가 존재하지 않습니다."),
    INVALID_NAME_CHANGE_PERIOD(-10010, "이름을 변경한 지 30일이 지나지 않았습니다."),

    //oauth 인증 문제
    INVALID_OAUTH_FORMAT(-10100, "서버 오류, OAuth2 인증시 요청 형식이 잘못 되었습니다."),
    INVALID_OAUTH_PROVIDER(-10102, "지원하지 않는 OAuth2 플랫폼입니다."),

    //token 관련 문제
    INVALID_TOKEN(-10200, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(-10201, "만료된 토큰입니다."),


    // image 관련 문제
    INVALID_IMAGE(-10300, "유효하지 않은 이미지입니다."),
    NOT_EXIST_IMAGE(-10301, "이미지가 존재하지 않습니다.");



    private final int code;
    private final String message;
}
