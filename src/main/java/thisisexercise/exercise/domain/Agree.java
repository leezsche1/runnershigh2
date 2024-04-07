package thisisexercise.exercise.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Agree {

    private int firstReqAgree;
    private int secondReqAgree;

    private int firstSelAgree;
    private int secondSelAgree;
    private int thirdSelAgree;

    protected Agree() {

    }

    public Agree(int firstSelAgree, int secondSelAgree, int thirdSelAgree) {
        this.firstSelAgree = firstSelAgree;
        this.secondSelAgree = secondSelAgree;
        this.thirdSelAgree = thirdSelAgree;
    }

    public Agree getAgree(int firstSelAgree, int secondSelAgree, int thirdSelAgree) {

        Agree agree = new Agree(firstSelAgree, secondSelAgree, thirdSelAgree);

        return agree;

    }

}
