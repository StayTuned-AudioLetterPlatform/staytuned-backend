package com.staytuned.staytuned.security.oauth;

import com.staytuned.staytuned.security.config.AppProperties;
import com.staytuned.staytuned.security.jwt.JwtUtil;
import com.staytuned.staytuned.security.oauth.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

import static com.staytuned.staytuned.security.oauth.CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final AppProperties appProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        log.info(targetUrl);

        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
//        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
//            log.debug("We've got an Unauthorized Redirect URI and can't proceed with the authentication");
//        }
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        String accessToken = jwtUtil.generateAccessToken(authenticationToJwtClaims(authentication));
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access_token", accessToken) //
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private HashMap<String, Object> authenticationToJwtClaims(final Authentication authentication){
        CustomOAuth2User userPrincipal =  (CustomOAuth2User)authentication.getPrincipal();
        String email = userPrincipal.getAttribute("email");
        String name = userPrincipal.getNickname();
        Long code = userPrincipal.getUserCd();

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("name", name);
        claims.put("code", code);

        return claims;
    }

//    private boolean isAuthorizedRedirectUri(String uri) {
//        URI clientRedirectUri = URI.create(uri);
//        String authorizedRedirectUri = appProperties.getAuthorizedRedirectUri();
//        log.info(authorizedRedirectUri);
//        URI authorizedURI = URI.create(authorizedRedirectUri);
//        return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
//                && authorizedURI.getPort() == clientRedirectUri.getPort();
//    }

}
