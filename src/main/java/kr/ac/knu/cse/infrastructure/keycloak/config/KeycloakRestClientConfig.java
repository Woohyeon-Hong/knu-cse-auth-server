package kr.ac.knu.cse.infrastructure.keycloak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

//HTTP Client for keycloak REST Admin APIs
@Configuration
public class KeycloakRestClientConfig {

    @Bean
    public RestClient keycloakRestClient() {
        return RestClient.builder().build();
    }
}
