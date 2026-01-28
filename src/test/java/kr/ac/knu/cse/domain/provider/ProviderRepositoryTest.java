package kr.ac.knu.cse.domain.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import kr.ac.knu.cse.domain.student.Student;
import kr.ac.knu.cse.domain.student.StudentRepository;
import kr.ac.knu.cse.support.JpaIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@JpaIntegrationTest
class ProviderRepositoryTest {

    @Autowired
    ProviderRepository providerRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    EntityManager em;

    void flushAndClear() {
        em.flush();
        em.clear();
    }

    @BeforeEach
    void beforeEach() {
        Student student = Student.of("학생1", "2022111111");
        studentRepository.save(student);
        flushAndClear();
    }

    @DisplayName("계정을 생성할 때 (provider_name, provider_key) 쌍이 중복되면, DB 반영 시점에 예외가 발생한다.")
    @Test
    void of() {
        //given
        Long studentId = studentRepository.findByStudentNumber("2022111111").get().getId();
        String providerName = "google";
        String providerKey = "providerKey";

        Provider provider1 = Provider.of(
                "test1@knu.ac.kr",
                providerName,
                providerKey,
                studentId
                );

        providerRepository.save(provider1);
        flushAndClear();

        Provider provider2 = Provider.of(
                "test2@knu.ac.kr",
                providerName,
                providerKey,
                studentId
        );

        //when && then
        assertThatThrownBy(() -> {
            providerRepository.save(provider2);
            flushAndClear();
        });
    }

    @DisplayName("providerName과 providerKey를 통해 계정을 조회한다.")
    @Test
    void findByProviderNameAndProviderKey() {
        //given
        Long studentId = studentRepository.findByStudentNumber("2022111111").get().getId();
        Provider provider = Provider.of(
                "test@knu.ac.kr",
                "google",
                "providerKeyTest",
                studentId);

        Provider saved = providerRepository.save(provider);
        flushAndClear();

        //when
        Optional<Provider> result = providerRepository.findByProviderNameAndProviderKey(
                "google",
                "providerKeyTest"
        );

        //then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(saved);
    }

    @DisplayName("학번으로 학생 계정을 조회한다.")
    @Test
    void findAllByStudentId() {
        //given
        Long studentId = studentRepository.findByStudentNumber("2022111111").get().getId();
        Provider provider = Provider.of(
                "test@knu.ac.kr",
                "google",
                "providerKeyTest",
                studentId);

        Provider saved = providerRepository.save(provider);
        flushAndClear();

        //when
        Provider found = providerRepository.findAllByStudentId(studentId).get(0);

        //then
        assertThat(found).isEqualTo(saved);
    }
}