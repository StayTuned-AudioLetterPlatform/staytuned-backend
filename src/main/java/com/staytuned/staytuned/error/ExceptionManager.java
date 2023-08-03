package com.staytuned.staytuned.error;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(JwtNotFoundException.class)
    private ResponseEntity<ErrorResponse> handleAuthenticationException(JwtNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ErrorResponse> handleSignatureException() {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.UNSUPPORTED_JWT_TOKEN);
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJwtException() {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_JWT_TOKEN);
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException() {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.EXPIRED_JWT_TOKEN);
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
