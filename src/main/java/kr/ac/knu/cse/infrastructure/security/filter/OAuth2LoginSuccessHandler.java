package kr.ac.knu.cse.infrastructure.security.filter;

import static java.nio.charset.StandardCharsets.UTF_8;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import kr.ac.knu.cse.application.OAuthLoginService;
import kr.ac.knu.cse.application.dto.OAuthLoginResult;
import kr.ac.knu.cse.global.exception.BusinessException;
import kr.ac.knu.cse.global.exception.auth.InvalidOidcUserException;
import kr.ac.knu.cse.global.exception.auth.InvalidSessionException;
import kr.ac.knu.cse.infrastructure.security.support.FilterBusinessExceptionWriter;
import kr.ac.knu.cse.infrastructure.security.support.OidcUserInfoMapper;
import kr.ac.knu.cse.presentation.LoginController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final String SIGNUP_CALLBACK_URL = "/signup";

    private static final String COOKIE_ACCESS_TOKEN_NAME = "ACCESS_TOKEN";
    private static final String COOKIE_PATH_VALUE = "/";
    private static final int COOKIE_MAX_AGE = 60 * 60;
    private static final String COOKIE_SAME_SITE_VALUE = "LAX";
    private static final String HEADER_COOKIE_NAME = "Set-Cookie";

    private final OidcUserInfoMapper oidcUserInfoMapper;
    private final OAuthLoginService oAuthLoginService;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final FilterBusinessExceptionWriter filterBusinessExceptionWriter;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        try {
            OAuthLoginResult result = loginWith(authentication);

            if (result.isNewUser()) {
                redirectToSignup(response);
                return;
            }

            OAuth2AccessToken accessToken = extractAccessToken(
                    (OAuth2AuthenticationToken) authentication
            );

            setAccessTokenCookie(
                    response,
                    accessToken.getTokenValue()
            );
            redirectToClient(request, response);
        } catch (BusinessException e) {
            filterBusinessExceptionWriter.write(response, e);
        }
    }

    private OAuthLoginResult loginWith(Authentication authentication) {
        validateAuthentication(authentication);

        OidcUser oidcUser =
                (OidcUser) authentication.getPrincipal();

        return oAuthLoginService.login(
                oidcUserInfoMapper.map(oidcUser)
        );
    }

    public void validateAuthentication(Authentication authentication) {
        if (!(authentication instanceof OAuth2AuthenticationToken)
                || !(authentication.getPrincipal() instanceof OidcUser)
        ) {
            throw new InvalidOidcUserException();
        }
    }

    private void redirectToSignup(
            HttpServletResponse response
    ) throws IOException {
        response.sendRedirect(SIGNUP_CALLBACK_URL);
    }

    private OAuth2AccessToken extractAccessToken(
            OAuth2AuthenticationToken token
    ) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                token.getAuthorizedClientRegistrationId(),
                token.getName()
        );
        validateClient(client);

        return client.getAccessToken();
    }

    private void validateClient(OAuth2AuthorizedClient client) {
        if (client == null || client.getAccessToken() == null) {
            throw new InvalidOidcUserException();
        }
    }

    private void setAccessTokenCookie(
            HttpServletResponse response,
            String tokenValue) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_ACCESS_TOKEN_NAME, tokenValue)
                .httpOnly(true)
                .secure(true)
                .path(COOKIE_PATH_VALUE)
                .maxAge(COOKIE_MAX_AGE)
                .sameSite(COOKIE_SAME_SITE_VALUE)
                .build();

        response.addHeader(HEADER_COOKIE_NAME, cookie.toString());
    }

    private void redirectToClient(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new InvalidSessionException();
        }

        String redirectUri =
                (String) session.getAttribute(LoginController.SESSION_REDIRECT_URI);
        String state =
                (String) session.getAttribute(LoginController.SESSION_STATE);

        if (redirectUri == null || state == null) {
            throw new InvalidSessionException();
        }

        response.sendRedirect(redirectUri + "?state="
                + URLEncoder.encode(state, UTF_8));
    }
}
