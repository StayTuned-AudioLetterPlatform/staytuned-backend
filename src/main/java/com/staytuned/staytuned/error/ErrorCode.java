package com.staytuned.staytuned.error;

import io.jsonwebtoken.MalformedJwtException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //JWT
    TOKEN_DOSE_NOT_EXIT(404, "J000", "JWT token dose not exit"),
    INVALID_JWT_TOKEN(401, "J001", "Invalid JWT token" ),
    EXPIRED_JWT_TOKEN(401, "J002", "JWT token is expired" ),
    UNSUPPORTED_JWT_TOKEN(401, "J003", "JWT token is unsupported");


    private final int status;
    private final String message;
    private final String code;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}