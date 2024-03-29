package com.staytuned.staytuned.security.oauth.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
    private Long userCd;
    private String nickname;
    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
     *                         {@link #getAttributes()}
     */
    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey) {
        super(authorities, attributes, nameAttributeKey);
    }

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            Long userCd, String nickname) {
        super(authorities, attributes, nameAttributeKey);
        this.userCd = userCd;
        this.nickname = nickname;
    }

}
