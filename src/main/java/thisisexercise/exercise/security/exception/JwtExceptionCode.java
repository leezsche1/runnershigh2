package thisisexercise.exercise.security.exception;

import lombok.Getter;

public enum JwtExceptionCode {
    UNKNOWN_ERROR("0001", "UNKNOWN_ERROR"),
    NOT_FOUND_TOKEN("0002", "NOT_FOUND_TOKEN: Headers에 토큰 형식의 값 찾을 수 없음"),
    INVALID_TOKEN("0003", "INVALID_TOKEN: 유효하지 않은 토큰"),
    EXPIRED_TOKEN("0004", "EXPIRED_TOKEN: 기간이 만료된 토큰"),
    UNSUPPORTED_TOKEN("0005", "UNSUPPORTED_TOKEN: 지원하지 않는 토큰"),
    ;


    @Getter
    private String code;

    @Getter
    private String message;

    JwtExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
