package kr.ac.knu.cse.infrastructure.security.support;


import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieCreator {

    private static final String COOKIE_ACCESS_TOKEN_NAME = "ACCESS_TOKEN";
    private static final String COOKIE_PATH_VALUE = "/";
    private static final int COOKIE_MAX_AGE = 60 * 60;
    private static final String COOKIE_SAME_SITE_VALUE = "LAX";
    private static final String HEADER_COOKIE_NAME = "Set-Cookie";

    public ResponseCookie createWithValue(String value) {
        return ResponseCookie.from(COOKIE_ACCESS_TOKEN_NAME, value)
                .httpOnly(true)
                .secure(true)
                .path(COOKIE_PATH_VALUE)
                .maxAge(COOKIE_MAX_AGE)
                .sameSite(COOKIE_SAME_SITE_VALUE)
                .build();
    }

    public ResponseCookie create() {
        return ResponseCookie.from(COOKIE_ACCESS_TOKEN_NAME, "value")
                .httpOnly(true)
                .secure(true)
                .path(COOKIE_PATH_VALUE)
                .maxAge(0)
                .sameSite(COOKIE_SAME_SITE_VALUE)
                .build();
    }
}
