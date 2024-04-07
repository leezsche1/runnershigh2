package thisisexercise.exercise.handler;

import lombok.Getter;

@Getter
public enum CustomExceptionCode {
    LOGIN_FAIL("1001", "로그인 실패입니다. ID나 비밀번호를 확인해주세요"),
    EXISTING_ID("1002", "이미 존재하는 ID입니다."),
    NULL_FAIL("1003", "모든 항목을 반드시 입력해주세요"),
    REFRESH_FAIL("1004", "존재하지 않는 refreshToken값입니다."),  //로그아웃과 accessToken재요청시 필요한 값
    PHONE_NUMBER_FAIL("1005", "휴대폰 번호를 입력해주세요"),
    AUTH_NUMBER_FAIL("1006", "올바른 인증번호를 입력해주세요"),
    ;

    private String code;
    private String message;


    CustomExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
