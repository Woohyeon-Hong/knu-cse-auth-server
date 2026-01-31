package kr.ac.knu.cse.application.auth.dto;

import kr.ac.knu.cse.domain.role.RoleType;

public record OAuthLoginResult(
        Long studentId,
        RoleType role
) {}
