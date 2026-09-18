package com.cavosh.api_cafe.shared.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret = "claveSecretaBase64ParaJWTAutenticacionCafeteria2026";
    private long expirationMs = 86400000;
}