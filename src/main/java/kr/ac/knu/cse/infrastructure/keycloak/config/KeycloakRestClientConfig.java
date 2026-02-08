package kr.ac.knu.cse.infrastructure.keycloak.config;

import java.net.http.HttpClient;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

//HTTP Client for keycloak REST Admin APIs
@Configuration
public class KeycloakRestClientConfig {

    private final static int CONNECT_TIMEOUT = 5;
    private final static int READ_TIMEOUT = 10;

    @Bean
    public RestClient keycloakRestClient() {
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(
                HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(CONNECT_TIMEOUT))
                        .build()
        );

        factory.setReadTimeout(Duration.ofSeconds(READ_TIMEOUT));

        return RestClient.builder()
                .requestFactory(factory)
                .build();
    }
}
