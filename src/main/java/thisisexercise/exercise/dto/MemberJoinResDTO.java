package thisisexercise.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberJoinResDTO {

    private Long id;
    private String email;
    private String memberPwd;
    private String role;
    private String memberPhone;

}
