package econovation.moongtaengi.study.domain.submission;

import static econovation.moongtaengi.study.domain.submission.SubmissionFixture.aSubmission;
import static org.assertj.core.api.Assertions.assertThat;

import econovation.moongtaengi.study.domain.StudyProcessFixture;
import econovation.moongtaengi.study.domain.assignment.Assignment;
import econovation.moongtaengi.study.domain.assignment.AssignmentFixture;
import econovation.moongtaengi.study.domain.assignment.AssignmentRepository;
import econovation.moongtaengi.study.domain.process.StudyProcess;
import econovation.moongtaengi.study.domain.process.StudyProcessRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class SubmissionRepositoryTest {

    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private StudyProcessRepository studyProcessRepository;

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

    @Test
    @DisplayName("제출물 ID로 연관된 스터디 ID를 조회한다.")
    void 제출물ID로_스터디ID_조회_성공() {
        //given
        Long expectedStudyId = 777L;
        Long submitterId = 1L;

        StudyProcess process = StudyProcessFixture.aStudyProcess(expectedStudyId);
        studyProcessRepository.save(process);

        Assignment assignment = AssignmentFixture.anAssignment()
                .processId(process.getId())
                .build();
        assignmentRepository.save(assignment);

        Submission submission = SubmissionFixture.aSubmission()
                        .assignmentId(assignment.getId())
                        .submitterId(submitterId)
                        .build();
        submissionRepository.save(submission);

        //when
        Optional<Long> result = submissionRepository.findStudyIdById(submission.getId());

        //then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedStudyId);
    }
}
