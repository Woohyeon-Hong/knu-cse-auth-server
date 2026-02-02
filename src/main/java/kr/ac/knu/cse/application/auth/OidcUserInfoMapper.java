package kr.ac.knu.cse.application.auth;

import kr.ac.knu.cse.application.auth.dto.OAuthUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Component
public class OidcUserInfoMapper {

    private static final String REQUIRED_EMAIL_DOMAIN = "@knu.ac.kr";
    private static final String PROVIDER_NAME = "KEYCLOAK";


    public OAuthUserInfo map(OidcUser oidcUser) {
        validateOidcUser(oidcUser);

        String providerName = PROVIDER_NAME;
        String providerKey = oidcUser.getSubject();
        String name = extractName(oidcUser);

        String email = extractEmail(oidcUser);
        validateEmailDomain(email);

        return OAuthUserInfo.of(
                providerName,
                providerKey,
                email,
                name
        );
    }

    private void validateOidcUser(OidcUser oidcUser) {
        if (oidcUser == null) {
            throw new IllegalArgumentException("OidcUser must not be null");
        }
    }

    private String extractEmail(OidcUser oidcUser) {
        String email = oidcUser.getEmail();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Missing email claim");
        }

        return email;
    }

    private String extractName(OidcUser oidcUser) {
        String name = oidcUser.getFullName();

        if (name != null && !name.isBlank()) {
            return name;
        }

        return oidcUser.getPreferredUsername();
    }

    private void validateEmailDomain(String email) {
        if (!email.endsWith(REQUIRED_EMAIL_DOMAIN)) {
            throw new IllegalArgumentException(
                    "Invalid email domain: " + email
            );
        }
    }
}
