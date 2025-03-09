package com.criclab.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {
    private String jwtSecret;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;
}