package econovation.moongtaengi.study.domain.submission;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class SubmissionFixture {

    public static final Long DEFAULT_ASSIGNMENT_ID = 1L;
    public static final Long DEFAULT_SUBMITTER_ID = 1L;
    public static final SubmissionContent DEFAULT_CONTENT = new SubmissionContent("테스트 제출 내용");
    public static final LocalDateTime DEFAULT_CURRENT_TIME = LocalDateTime.now();
    public static final LocalDateTime DEFAULT_DEADLINE = LocalDateTime.now().plusDays(5);

    public static Submission.SubmissionBuilder aSubmission() {
        return Submission.builder()
                .assignmentId(DEFAULT_ASSIGNMENT_ID)
                .submitterId(DEFAULT_SUBMITTER_ID)
                .content(DEFAULT_CONTENT)
                .attachments(new ArrayList<>())
                .currentDateTime(DEFAULT_CURRENT_TIME)
                .assignmentDeadline(DEFAULT_DEADLINE);
    }
}