package com.acme.payments.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidIso20022MessageException.class)
  public ResponseEntity<Map<String, Object>> handleInvalidIso20022(InvalidIso20022MessageException ex) {
    return ResponseEntity.badRequest().body(
            Map.of(
                    "timestamp", Instant.now().toString(),
                    "status", HttpStatus.BAD_REQUEST.value(),
                    "error", "Invalid ISO 20022 Message",
                    "errorCode", ex.getErrorCode(),
                    "message", ex.getMessage()
            )
    );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
    return ResponseEntity.internalServerError().body(
            Map.of(
                    "timestamp", Instant.now().toString(),
                    "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "error", "Internal Server Error",
                    "message", ex.getMessage()
            )
    );
  }
}
