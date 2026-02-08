package kr.ac.knu.cse.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

import kr.ac.knu.cse.application.dto.SignupCommand;
import kr.ac.knu.cse.application.dto.SignupResponse;
import kr.ac.knu.cse.domain.provider.Provider;
import kr.ac.knu.cse.domain.provider.ProviderRepository;
import kr.ac.knu.cse.domain.student.Grade;
import kr.ac.knu.cse.domain.student.Student;
import kr.ac.knu.cse.domain.student.StudentRepository;
import kr.ac.knu.cse.global.exception.auth.AlreadySignedUpException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SignupServiceTest {

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private SignupService signupService;

    @DisplayName("신규 회원이면 Student와 Provider를 저장하고 studentId를 반환한다.")
    @Test
    void signup_success() {
        // given
        SignupCommand command = new SignupCommand(
                "test@knu.ac.kr",
                "keycloak",
                "provider-key",
                "컴퓨터학부",
                "홍길동",
                "2022111111",
                Grade.SECOND
        );

        given(providerRepository.findByProviderNameAndProviderKey(
                "keycloak",
                "provider-key"
        )).willReturn(Optional.empty());

        given(studentRepository.save(any(Student.class)))
                .willAnswer(invocation -> {
                    Student student = invocation.getArgument(0);
                    student.setIdForTest(1L);
                    return student;
                });

        // when
        SignupResponse response = signupService.signup(command);

        // then
        assertThat(response.studentId()).isEqualTo(1L);

        verify(studentRepository).save(any(Student.class));
        verify(providerRepository).save(any(Provider.class));
    }

    @DisplayName("이미 가입된 Provider가 있으면 예외가 발생한다.")
    @Test
    void signup_alreadySignedUp_validate() {
        // given
        SignupCommand command = new SignupCommand(
                "test@knu.ac.kr",
                "keycloak",
                "provider-key",
                "컴퓨터학부",
                "홍길동",
                "2022111111",
                Grade.SECOND
        );

        given(providerRepository.findByProviderNameAndProviderKey(
                "keycloak",
                "provider-key"
        )).willReturn(Optional.of(any(Provider.class)));

        // when & then
        assertThatThrownBy(() -> signupService.signup(command))
                .isInstanceOf(AlreadySignedUpException.class);

        verify(studentRepository, never()).save(any());
        verify(providerRepository, never()).save(any());
    }

    @DisplayName("Provider 저장 시 무결성 예외가 발생하면 AlreadySignedUpException을 던진다.")
    @Test
    void signup_alreadySignedUp_onSave() {
        // given
        SignupCommand command = new SignupCommand(
                "test@knu.ac.kr",
                "keycloak",
                "provider-key",
                "컴퓨터학부",
                "홍길동",
                "2022111111",
                Grade.SECOND
        );

        given(providerRepository.findByProviderNameAndProviderKey(
                "keycloak",
                "provider-key"
        )).willReturn(Optional.empty());

        given(studentRepository.save(any(Student.class)))
                .willAnswer(invocation -> {
                    Student student = invocation.getArgument(0);
                    student.setIdForTest(1L);
                    return student;
                });

        given(providerRepository.save(any(Provider.class)))
                .willThrow(new DataIntegrityViolationException("duplicate"));

        // when & then
        assertThatThrownBy(() -> signupService.signup(command))
                .isInstanceOf(AlreadySignedUpException.class);
    }
}
