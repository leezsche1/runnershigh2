package thisisexercise.exercise.handler;

import java.util.Map;

public class CustomValidationException2 extends RuntimeException{

    private static final long serialVersionUID = 2L;

    private Map<String, String> errorMap;

    public CustomValidationException2(String message, Map<String, String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
    }

}
