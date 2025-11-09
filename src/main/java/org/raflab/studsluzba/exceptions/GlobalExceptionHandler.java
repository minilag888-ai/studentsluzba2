package org.raflab.studsluzba.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        // Ako poruka sadrži "ne postoji" - vrati 404
        if (ex.getMessage().contains("ne postoji")) {
            body.put("status", 404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }

        // Ako poruka sadrži validaciju (ESPB) - vrati 400
        if (ex.getMessage().contains("ESPB") || ex.getMessage().contains("može biti")) {
            body.put("status", 400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        // Ostalo - 500
        body.put("status", 500);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}