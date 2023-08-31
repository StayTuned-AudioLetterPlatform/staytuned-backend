package com.staytuned.staytuned.security.jwt;

import com.staytuned.staytuned.domain.Role;
import com.staytuned.staytuned.domain.User;
import com.staytuned.staytuned.domain.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    String email ="test@test.com";
    String name = "test";
    Long code = 1L;

    @BeforeEach
    public void setup() {
        userRepository.save(User.builder()
                .name("test")
                .email("test@test.com")
                .picture(null)
                .role(Role.USER)
                .build());
    }

    @DisplayName("jwt 토큰을 생성한다")
    @Test
    public void createJwtTokenTest(){
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("name", name);
        claims.put("code", code);

        String jwt = jwtUtil.generateAccessToken(claims);
        assertThat(jwtUtil.extractEmail(jwt)).isEqualTo(email);
    }

    @DisplayName("jwt 토큰을 검증한다 - 성공 테스트")
    @Test
    public void successAuthenticationTokenTest() {
        String jwt = createJwtToken();
        jwtUtil.isValidToken(jwt);
    }

    private String createJwtToken(){
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("name", name);
        claims.put("code", code);

        return jwtUtil.generateAccessToken(claims);
    }

    @DisplayName("jwt 토큰을 검증한다 - Access Time 만료된 토큰")
    @Test
    public void failAuthenticationTokenTest(){
        // given: access time 만료된 토큰
        String jwt = "eyJ0eXBlIjoiSldUIiwiYWxnb3JpdGhtIjoiSFM1MTIiLCJhbGciOiJIUzUxMiJ9.eyJjb2RlIjoxLCJuYW1lIjoidGVzdCIsImVtYWlsIjoidGVzdEB0ZXN0LmNvbSIsImlhdCI6MTY5MzM4NDQ4MiwiZXhwIjoxNjkzMzg0NTQyfQ.8m54a6z46LrMSIyTZdbLTiT-V7uWaS3K8f2o0BxynjAPRjbD1tmaUZ38mijGC4pHTFMPkCj0s0p94gxTZPSq1Q";
        assertThatThrownBy(() -> jwtUtil.isValidToken(jwt))
                .isInstanceOf(ExpiredJwtException.class);
    }
}
