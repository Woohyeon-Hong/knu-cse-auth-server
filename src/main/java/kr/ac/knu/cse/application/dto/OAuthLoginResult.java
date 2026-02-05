package kr.ac.knu.cse.application.dto;

public record OAuthLoginResult(
        Long studentId,
        boolean isNewUser
) {
    public static OAuthLoginResult existingUser(Long studentId) {
        return new OAuthLoginResult(studentId, false);
    }

    public static OAuthLoginResult newUser() {
        return new OAuthLoginResult(null, true);
    }
}
