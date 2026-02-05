package kr.ac.knu.cse.application;

import kr.ac.knu.cse.application.dto.SignupCommand;
import kr.ac.knu.cse.application.dto.SignupResponse;
import kr.ac.knu.cse.domain.provider.Provider;
import kr.ac.knu.cse.domain.provider.ProviderRepository;
import kr.ac.knu.cse.domain.student.Student;
import kr.ac.knu.cse.domain.student.StudentRepository;
import kr.ac.knu.cse.global.exception.auth.AlreadySignedUpException;
import kr.ac.knu.cse.global.exception.provisioning.InvalidStudentNumberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final ProviderRepository providerRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public SignupResponse signup(SignupCommand command) {
        validate(command.providerName(), command.providerKey());

        Student student = Student.of(
                command.major(),
                command.name(),
                command.studentNumber(),
                command.grade()
        );
        studentRepository.save(student);

        Provider provider = Provider.of(
                command.email(),
                command.providerName(),
                command.providerKey(),
                student.getId()
        );
        providerRepository.save(provider);

        return new SignupResponse(student.getId());
    }

    private void validate(
            String providerName,
            String providerKey
    ) {
        if (providerRepository.findByProviderNameAndProviderKey(
                providerName,
                providerKey
        ).isPresent()) {
            throw new AlreadySignedUpException();
        }
    }
}
