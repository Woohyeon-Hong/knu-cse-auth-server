package kr.ac.knu.cse.presentation.auth;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpHeaders.LOCATION;

import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LogoutController {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "ACCESS_TOKEN";

    @Value("${keycloak.base-url}")
    private String keycloakBaseUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.post-logout-redirect-uri}")
    private String postLogoutRedirectUri;

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response)  {
        deleteAccessTokenCookie(response);
        SecurityContextHolder.clearContext();

        String redirectUrl = buildKeycloakLogoutUrl();

        return ResponseEntity
                .status(302)
                .header(LOCATION, redirectUrl)
                .build();
    }

    private void deleteAccessTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, "")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .maxAge(0)
                .build();

        response.addHeader(SET_COOKIE, cookie.toString());
    }

    private String buildKeycloakLogoutUrl() {
        String encodedRedirect = URLEncoder.encode(
                postLogoutRedirectUri,
                StandardCharsets.UTF_8
        );

        return keycloakBaseUrl
                + "/realms/" + realm
                + "/protocol/openid-connect/logout"
                + "?post_logout_redirect_uri=" + encodedRedirect;
    }
}
