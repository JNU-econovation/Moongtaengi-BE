package econovation.moongtaengi.study.domain.assignment;

import econovation.moongtaengi.study.domain.StudyFixture;
import java.time.LocalDate;

public class AssignmentFixture {
    public static final Long DEFAULT_PROCESS_ID = 1L;
    public static final Long DEFAULT_ASSIGNEE_ID = 1L;
    public static final AssignmentDescription DEFAULT_DESCRIPTION = new AssignmentDescription("테스트 과제");

    public static final LocalDate BASE_DATE = StudyFixture.FIXED_DATE;

    public static Assignment.AssignmentBuilder anAssignment() {
        return Assignment.builder()
                .processId(DEFAULT_PROCESS_ID)
                .assigneeId(DEFAULT_ASSIGNEE_ID)
                .description(DEFAULT_DESCRIPTION)
                .deadline(AssignmentDeadline.create(
                        BASE_DATE.plusDays(7).atStartOfDay(),
                        BASE_DATE,
                        BASE_DATE.plusDays(14)
                ));
    }
}
