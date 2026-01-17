package econovation.moongtaengi.study.domain.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AssignmentRepositoryTest {
    @Autowired
    private AssignmentRepository assignmentRepository;

    @Test
    @DisplayName("프로세스 ID와 과제 수행자 ID로 과제 존재 여부를 확인한다")
    void 중복_과제_확인_성공() {
        //given
        Long processId = 100L;
        Long assigneeId = 1L;
        Long noAssigneeId = 999L;

        Assignment assignment = AssignmentFixture.anAssignment()
                .processId(processId)
                .assigneeId(assigneeId)
                .build();

        assignmentRepository.save(assignment);

        //when
        boolean exists = assignmentRepository.existsByProcessIdAndAssigneeId(processId, assigneeId);
        boolean notExists = assignmentRepository.existsByProcessIdAndAssigneeId(processId, noAssigneeId);

        //then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
