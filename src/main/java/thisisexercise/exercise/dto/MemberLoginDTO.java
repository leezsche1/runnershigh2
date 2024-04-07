package thisisexercise.exercise.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginDTO {

    @NotNull(message = "email값은 필수입니다.")
    private String memberEmail;

    @NotNull(message = "password값은 필수입니다.")
    private String memberPwd;

}
