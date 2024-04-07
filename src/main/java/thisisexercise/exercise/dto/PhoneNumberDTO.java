package thisisexercise.exercise.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneNumberDTO {

    @NotNull(message = "번호를 입력해주세요")
    public String phoneNumber;


}
