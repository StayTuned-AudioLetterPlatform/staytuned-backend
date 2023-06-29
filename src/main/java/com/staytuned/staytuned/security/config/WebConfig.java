package com.staytuned.staytuned.security.config;

import com.staytuned.staytuned.security.jwt.JwtAuthorizationArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    private final JwtAuthorizationArgumentResolver jwtAuthorizationArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtAuthorizationArgumentResolver);
    }
}
