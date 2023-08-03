package com.staytuned.staytuned.error;

public class JwtNotFoundException extends BusinessException {
    public JwtNotFoundException() {
        super(ErrorCode.TOKEN_DOSE_NOT_EXIT);
    }
}

