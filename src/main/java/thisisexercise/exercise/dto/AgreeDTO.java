package thisisexercise.exercise.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgreeDTO {

    @NotNull
    private int firstSelAgree;

    @NotNull
    private int secondSelAgree;

    @NotNull
    private int thirdSelAgree;



}
