package thisisexercise.exercise.handler;

public class CustomValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private CustomExceptionCode customExceptionCode;

    public CustomValidationException(CustomExceptionCode customExceptionCode) {
        this.customExceptionCode = customExceptionCode;
    }

    public CustomExceptionCode getCustomExceptionCode() {
        return customExceptionCode;
    }

}
