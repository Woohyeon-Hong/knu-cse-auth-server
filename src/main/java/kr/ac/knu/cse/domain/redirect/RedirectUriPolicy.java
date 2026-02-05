package kr.ac.knu.cse.domain.redirect;

import kr.ac.knu.cse.global.exception.auth.InvalidRedirectUriException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedirectUriPolicy {

    private final RedirectUriProperties redirectUriProperties;

    public void validate(String redirectUri) {
        if (redirectUri == null || redirectUri.isBlank()) {
            throw new InvalidRedirectUriException();
        }

        if (!redirectUriProperties.redirectAllowlist().contains(redirectUri)) {
            throw new InvalidRedirectUriException();
        }
    }
}
