package kr.ac.knu.cse.infrastructure.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.ac.knu.cse.global.exception.auth.AccountDisabledException;
import kr.ac.knu.cse.global.exception.auth.InvalidOAuthClientException;
import kr.ac.knu.cse.global.exception.auth.InvalidOidcUserException;
import kr.ac.knu.cse.global.exception.auth.UnauthorizedOAuthClientException;
import kr.ac.knu.cse.global.exception.common.InternalServerErrorException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    private final static String ACCESS_DENIED = "access_denied";
    private final static String DISABLED_ACCOUNT = "Account is disabled";
    private final static String INVALID_CLIENT = "invalid_client";
    private final static String UNAUTHORIZED_CLIENT = "unauthorized_client";

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) {
        throw translate(exception);
    }

    private RuntimeException translate(AuthenticationException ex) {
        if (ex instanceof OAuth2AuthenticationException oauthEx) {
            return handleOAuth2Exception(oauthEx);
        }

        return new InternalServerErrorException();
    }

    private RuntimeException handleOAuth2Exception(
            OAuth2AuthenticationException ex
    ) {
        OAuth2Error error = ex.getError();

        String errorCode = error.getErrorCode();
        String description = error.getDescription();

        if (ACCESS_DENIED.equals(errorCode)
                && description != null
                && description.contains(DISABLED_ACCOUNT)) {
            return new AccountDisabledException();
        }
        if (ACCESS_DENIED.equals(errorCode)) {
            return new InvalidOidcUserException();
        }
        if (INVALID_CLIENT.equals(errorCode)) {
            return new InvalidOAuthClientException();
        }
        if (UNAUTHORIZED_CLIENT.equals(errorCode)) {
            return new UnauthorizedOAuthClientException();
        }

        return new InternalServerErrorException();
    }

}
