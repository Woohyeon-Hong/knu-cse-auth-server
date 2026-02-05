package kr.ac.knu.cse.presentation;

import kr.ac.knu.cse.application.SignupService;
import kr.ac.knu.cse.application.dto.SignupCommand;
import kr.ac.knu.cse.application.dto.SignupResponse;
import kr.ac.knu.cse.global.exception.auth.InvalidOidcUserException;
import kr.ac.knu.cse.presentation.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController {

    private static final String PROVIDER_NAME = "KEYCLOAK";


    private final SignupService signupService;

    @PostMapping
    public ResponseEntity<SignupResponse> signup(
            @AuthenticationPrincipal OidcUser oidcUser,
            @RequestBody SignupRequest request
    ) {
        if (oidcUser == null) {
            throw new InvalidOidcUserException();
        }

        SignupCommand command = new SignupCommand(
                PROVIDER_NAME,
                oidcUser.getSubject(),
                oidcUser.getEmail(),
                request.studentNumber(),
                oidcUser.getFullName(),
                request.major(),
                request.grade()
        );

        SignupResponse response = signupService.signup(command);

        return ResponseEntity.ok(response);
    }
}
