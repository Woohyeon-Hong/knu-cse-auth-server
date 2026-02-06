package kr.ac.knu.cse.infrastructure.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "internal")
public record InternalApiProperties(
        String apiKey
) {}
