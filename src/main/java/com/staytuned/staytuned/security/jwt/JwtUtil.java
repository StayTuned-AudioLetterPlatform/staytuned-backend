package com.staytuned.staytuned.security.jwt;

import com.staytuned.staytuned.security.oauth.dto.CustomOAuth2User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.staytuned.staytuned.security.jwt.TokenExpiredTime.ACCESS_TOKEN_EXPIRATION_TIME;
import static com.staytuned.staytuned.security.jwt.TokenExpiredTime.REFRESH_TOKEN_EXPIRATION_TIME;

@Slf4j
@Component
public class JwtUtil {
    // "Brarer 추가 해서 생성하기"
    private final Key key;

    public JwtUtil(@Value(value = "${app.jwt.secret}") final String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateAccessToken(final Authentication authentication) {
        return createToken(authentication, ACCESS_TOKEN_EXPIRATION_TIME.getValue());
    }

    public boolean isValidToken(final String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
//    // Jwt 로 인증정보를 조회
//    public Authentication getAuthentication(String token) {
//
//        Claims claims = extractAllClaims(token);
//
//        // 권한 정보가 없음
//        if (claims.get(ROLES) == null) {
//            throw new EntryPointException();
//        }
//
//        OAuth2User userDetails = CustomOAuth2UserService.loadUserByUsername(claims.getSubject());
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }

    public String extractEmail(final String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    public Long extractCode(final String token) {
        return extractAllClaims(token).get("code", Long.class);
    }

    private String createToken(final Authentication authentication, final int expiredTime) {
        CustomOAuth2User userPrincipal =  (CustomOAuth2User)authentication.getPrincipal();
        String email = userPrincipal.getAttribute("email");
        String name = userPrincipal.getNickname();
        Long code = userPrincipal.getUserCd();

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("name", name);
        claims.put("code", code);

        return Jwts.builder()
                .setHeader(settingHeaders())
                .setClaims(claims)
                .setIssuedAt(settingsDate(0))
                .setExpiration(settingsDate(expiredTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private Date settingsDate(final int plusTime) {
        return Date.from(LocalDateTime.now().plusHours(plusTime)
                .atZone(ZoneId.systemDefault())
                .toInstant()
        );
    }

    private Map<String, Object> settingHeaders() {
        final HashMap<String, Object> headers = new HashMap<>();
        headers.put("type", Header.JWT_TYPE);
        headers.put("algorithm", SignatureAlgorithm.HS512);
        return  headers;
    }


    private <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 토큰이 유효한 토큰인지 검사한 후, 토큰에 담긴 Payload 값을 가져온다.
    private Claims extractAllClaims(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
