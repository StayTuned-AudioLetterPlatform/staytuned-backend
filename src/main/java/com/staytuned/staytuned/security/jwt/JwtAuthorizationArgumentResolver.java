package com.staytuned.staytuned.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JwtAuthorizationArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Long resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("JwtAuthorizationArgumentResolver 동작!!");

        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (httpServletRequest != null) {
            String token = httpServletRequest.getHeader("Authorization");

            if (token != null && !token.trim().equals("")) {
                log.info("토큰 존재");
                // 토큰 있을 경우 검증
                if (jwtUtil.isValidToken(token)) {
                    log.info("토큰 검증 완료");
                    return jwtUtil.extractCode(token);
                }
            }
            // 토큰은 없지만 필수가 아닌 경우 체크
            LoginUser annotation = parameter.getParameterAnnotation(LoginUser.class);
            if (annotation != null && !annotation.required()) {
                // 필수가 아닌 경우 기본 객체 리턴
                return null;
            }
        }

        // 토큰 값이 없으면 에러
        throw new RuntimeException("권한 없음.");
    }

    //    private String resolveToken(HttpServletRequest request) { //토큰 꺼내오기
//        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
//
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//
//        return null;
//    }

}
