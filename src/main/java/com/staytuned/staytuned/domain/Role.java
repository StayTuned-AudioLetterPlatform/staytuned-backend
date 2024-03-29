package com.staytuned.staytuned.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    MASTER("ROLE_MASTER", "운영자"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;
}
