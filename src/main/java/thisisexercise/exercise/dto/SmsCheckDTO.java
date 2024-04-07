package thisisexercise.exercise.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsCheckDTO {

    @NotNull(message = "인증번호는 필수입니다.")
    private String authNumber;

    @NotNull(message = "휴대폰 번호는 필수입니다.")
    private String phoneNumber;
}
