package thisisexercise.exercise.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class MemberInfo {

    private String nickName;
    private String gender;

    private String height;
    private String weight;

    protected MemberInfo() {
    }
}
