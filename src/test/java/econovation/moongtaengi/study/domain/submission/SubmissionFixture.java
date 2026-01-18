package econovation.moongtaengi.study.domain.submission;

import econovation.moongtaengi.study.domain.StudyFixture;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SubmissionFixture {

    public static final Long DEFAULT_ASSIGNMENT_ID = 1L;
    public static final Long DEFAULT_SUBMITTER_ID = 1L;
    public static final SubmissionContent DEFAULT_CONTENT = new SubmissionContent("제출 완료");
    public static final LocalDateTime BASE_TIME = StudyFixture.FIXED_DATE.atStartOfDay();

    public static Submission.SubmissionBuilder aSubmission() {
        return Submission.builder()
                .assignmentId(DEFAULT_ASSIGNMENT_ID)
                .submitterId(DEFAULT_SUBMITTER_ID)
                .content(DEFAULT_CONTENT)
                .attachments(new ArrayList<>())
                .currentDateTime(BASE_TIME.plusDays(1))
                .assignmentDeadline(BASE_TIME.plusDays(7));
    }
}