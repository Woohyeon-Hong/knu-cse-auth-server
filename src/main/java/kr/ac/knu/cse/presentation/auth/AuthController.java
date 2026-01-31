package kr.ac.knu.cse.presentation.auth;

import kr.ac.knu.cse.application.auth.OAuthLoginService;
import kr.ac.knu.cse.application.auth.OidcUserInfoMapper;
import kr.ac.knu.cse.application.auth.dto.OAuthLoginResult;
import kr.ac.knu.cse.application.auth.dto.OAuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final OidcUserInfoMapper oidcUserInfoMapper;
    private final OAuthLoginService oAuthLoginService;


    @GetMapping("/login/success")
    public ResponseEntity<Void> loginSuccess(
            @AuthenticationPrincipal OidcUser oidcUser
    ) {
        OAuthUserInfo userInfo = oidcUserInfoMapper.map(oidcUser);
        OAuthLoginResult result = oAuthLoginService.login(userInfo);

        return ResponseEntity.ok().build();
    }
}
