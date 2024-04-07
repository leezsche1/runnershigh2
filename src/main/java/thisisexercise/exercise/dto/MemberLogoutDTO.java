package thisisexercise.exercise.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLogoutDTO {


    @NotNull(message = "값을 채워주세요")
    private String memberId;

    @NotNull(message = "값을 채워주세요")
    private String memberEmail;

    @NotNull(message = "값을 채워주세요")
    private String refreshToken;


}
