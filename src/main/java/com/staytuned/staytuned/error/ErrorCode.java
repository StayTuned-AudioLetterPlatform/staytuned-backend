package com.staytuned.staytuned.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //JWT
    TOKEN_DOSE_NOT_EXIT(HttpStatus.UNAUTHORIZED, "J000", "JWT token dose not exit"),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "J001", "Invalid JWT token" ),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "J002", "JWT token is expired" ),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "J003", "JWT token is unsupported");

    private final HttpStatus status;
    private final String message;
    private final String code;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
