package econovation.moongtaengi.study.domain.assignment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;

public class AssignmentFixture {
    public static final Long DEFAULT_PROCESS_ID = 1L;
    public static final Long DEFAULT_ASSIGNEE_ID = 1L;
    public static final String DEFAULT_CONTENT_VALUE = "테스트 내용";
    public static final AssignmentContent DEFAULT_CONTENT = new AssignmentContent(DEFAULT_CONTENT_VALUE);

    public static Assignment.AssignmentBuilder anAssignment() {
        LocalDate processStartDate = LocalDate.now();
        LocalDate processEndDate = processStartDate.plusDays(14);
        LocalDateTime requestDeadline = processStartDate.plusDays(7).atStartOfDay();

        return Assignment.builder()
                .processId(DEFAULT_PROCESS_ID)
                .assigneeId(DEFAULT_ASSIGNEE_ID)
                .content(DEFAULT_CONTENT)
                .deadline(AssignmentDeadline.create(
                        requestDeadline,
                        processStartDate,
                        processEndDate
                ));
    }

    public static Assignment createSavedAssignment(Long id) {
        Assignment assignment = anAssignment().build();
        ReflectionTestUtils.setField(assignment, "id", id);
        return assignment;
    }
}
