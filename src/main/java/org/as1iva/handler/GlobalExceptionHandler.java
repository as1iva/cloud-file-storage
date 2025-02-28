package org.as1iva.handler;

import org.as1iva.exception.InvalidDataException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleBadCredentialsException(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "status", 403,
                "message", e.getMessage()
        ));
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<?> handleInvalidDataException(InvalidDataException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", 400,
                "message", e.getMessage()
        ));
    }
}
