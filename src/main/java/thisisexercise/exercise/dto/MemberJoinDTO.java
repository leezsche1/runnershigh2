package thisisexercise.exercise.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinDTO {
    @NotNull(message = "이메일값은 필수입니다.")
    private String memberEmail;

    @NotNull(message = "이름값은 필수입니다.")
    private String memberNm;

    @NotNull(message = "비밀번호는 필수입니다.")
    private String memberPwd;

    @NotNull(message = "생년월일은 필수입니다.")
    private String birth;

    @NotNull(message = "Mobile번호는 필수입니다.")
    private String memberPhone;

    private AgreeDTO agreeDTO;

}
