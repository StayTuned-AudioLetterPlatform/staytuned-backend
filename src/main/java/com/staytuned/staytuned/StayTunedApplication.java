package com.staytuned.staytuned;

import com.staytuned.staytuned.security.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class StayTunedApplication {
    public static void main(String[] args) {
        SpringApplication.run(StayTunedApplication.class, args);
    }
}
