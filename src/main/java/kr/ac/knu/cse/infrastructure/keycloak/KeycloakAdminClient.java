package kr.ac.knu.cse.infrastructure.keycloak;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class KeycloakAdminClient {

    private final RestClient keycloakRestClient;
    private final KeycloakAdminProperties keycloakAdminProperties;

    public void upsertStudentIdAttribute(String keycloakUserId, Long studentId) {
        String token = getAdminAccessToken();
        Map<String, Object> payload = createAttributes(studentId);
        requestUpdateOfToken(keycloakUserId, token, payload);
    }

    private String getAdminAccessToken() {
        Map<String, Object> response = requestToken();
        validateTokenResponse(response);
        return String.valueOf(response.get("access_token"));
    }

    private Map<String, Object> requestToken() {
        return keycloakRestClient.post()
                .uri(keycloakAdminProperties.baseUrl()
                        + "/realms/" + keycloakAdminProperties.realm()
                        + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                        new LinkedMultiValueMap<>() {{
                            add("grant_type", "client_credentials");
                            add("client_id", keycloakAdminProperties.admin().clientId());
                            add("client_secret", keycloakAdminProperties.admin().clientSecret());
                        }}
                )
                .retrieve()
                .body(Map.class);
    }

    private void validateTokenResponse(Map<String, Object> tokenResponse) {
        if (tokenResponse == null || tokenResponse.get("access_token") == null) {
            throw new IllegalStateException("Failed to get Keycloak admin access token");
        }
    }

    private Map<String, Object> createAttributes(Long studentId) {
        return Map.of(
                "attributes", Map.of(
                        "studentId", List.of(String.valueOf(studentId))
                )
        );
    }

    private void requestUpdateOfToken(
            String keycloakUserId,
            String token,
            Map<String, Object> payload) {

        keycloakRestClient.put()
                .uri(keycloakAdminProperties.baseUrl()
                        + "/admin/realms/" + keycloakAdminProperties.realm()
                        + "/users/" + keycloakUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(token))
                .body(payload)
                .retrieve()
                .toBodilessEntity();
    }

    public void ensureRealmRole(String keycloakUserId, String roleName) {
        String token = getAdminAccessToken();

        Map<String, Object> role = requestOriginalRole(roleName, token);
        validateRole(roleName, role);

        requestUpdateOfRole(keycloakUserId, token, role);
    }

    private Map<String, Object> requestOriginalRole(String roleName, String token) {
        return keycloakRestClient.get()
                .uri(keycloakAdminProperties.baseUrl()
                        + "/admin/realms/" + keycloakAdminProperties.realm()
                        + "/roles/" + roleName)
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .body(Map.class);
    }

    private void validateRole(String roleName, Map<String, Object> role) {
        if (role == null) {
            throw new IllegalStateException("Keycloak role not found: " + roleName);
        }
    }

    private void requestUpdateOfRole(String keycloakUserId, String token, Map<String, Object> role) {
        keycloakRestClient.post()
                .uri(keycloakAdminProperties.baseUrl()
                        + "/admin/realms/" + keycloakAdminProperties.realm()
                        + "/users/" + keycloakUserId + "/role-mappings/realm")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(token))
                .body(List.of(role))
                .retrieve()
                .toBodilessEntity();
    }
}

