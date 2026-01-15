package econovation.moongtaengi.study.domain.submission;

import static econovation.moongtaengi.study.domain.submission.SubmissionFixture.aSubmission;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class SubmissionRepositoryTest {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Test
    @DisplayName("과제 ID로 제출 내역 존재 여부를 확인한다.")
    void 제출물_존재_확인_성공() {
        //given
        Long assignmentId = 100L;

        Submission submission = aSubmission()
                .assignmentId(assignmentId)
                .build();

        submissionRepository.save(submission);

        //when
        boolean exists = submissionRepository.existsByAssignmentId(assignmentId);
        boolean notExists = submissionRepository.existsByAssignmentId(999L);

        //then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
