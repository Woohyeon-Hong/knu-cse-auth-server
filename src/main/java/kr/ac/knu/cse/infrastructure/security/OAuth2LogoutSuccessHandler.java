package kr.ac.knu.cse.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2LogoutSuccessHandler implements LogoutSuccessHandler {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "ACCESS_TOKEN";

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        ResponseCookie deleteCookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, "")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("LAX")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}

