package org.kshitij.Application.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HashMap<String, String>> handleValidationException(
            MethodArgumentNotValidException e) {
        HashMap<String, String>errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().
                forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<HashMap<String, String>> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException e
    ){
        HashMap<String, String>errors = new HashMap<>();
        log.warn("Email already exists {}",  e.getMessage(), e);
        errors.put("Message", "Another patient is already registered with this email");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(PatientNotExistsException.class)
    public ResponseEntity<HashMap<String, String>> handlePatientNotExistsException(
            PatientNotExistsException e
    ){
        HashMap<String, String>errors = new HashMap<>();
        log.warn("null id of patient: {}",  e.getMessage());
        errors.put("Message", "Patient not registered with the id");
        return ResponseEntity.badRequest().body(errors);
    }
}
