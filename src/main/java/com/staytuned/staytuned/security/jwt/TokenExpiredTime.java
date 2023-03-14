package com.staytuned.staytuned.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenExpiredTime {
    ACCESS_TOKEN_EXPIRATION_TIME("Access 토근 만료 시간",  30),  // 30분
    REFRESH_TOKEN_EXPIRATION_TIME("Refresh 토큰 만료 시간", 60 * 24 * 7); // (1분) * 60 * 24 * 7 : 7일

    private final String description;
    private final int value;
}

