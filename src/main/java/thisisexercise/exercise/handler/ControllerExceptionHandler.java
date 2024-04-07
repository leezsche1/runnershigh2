package thisisexercise.exercise.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity validationException(CustomValidationException e) {
        CommonResDTO<Map<String, String>> commonResDTO = new CommonResDTO<>(e.getCustomExceptionCode().getCode(), e.getCustomExceptionCode().getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResDTO);
    }

}
