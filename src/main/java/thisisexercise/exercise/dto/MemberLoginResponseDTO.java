package thisisexercise.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResponseDTO {

    private String accessToken;
    private String refreshToken;

    private Long id;
    private String memberEmail;
    private String name;

}
