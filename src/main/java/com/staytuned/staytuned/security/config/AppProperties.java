package com.staytuned.staytuned.security.config;

import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;



@NoArgsConstructor
@ConfigurationProperties(prefix = "app.oauth2")
public class AppProperties {
    private String authorizedRedirectUri;

    public String getAuthorizedRedirectUri() {
        return authorizedRedirectUri;
    }

    public void setAuthorizedRedirectUri(String authorizedRedirectUri) {
        this.authorizedRedirectUri = authorizedRedirectUri;
    }
}
