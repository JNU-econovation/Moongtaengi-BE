package econovation.moongtaengi.study.domain.submission;

import static econovation.moongtaengi.study.domain.submission.SubmissionFixture.aSubmission;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SubmissionTest {
    @Test
    @DisplayName("모든 필수 값이 정상일 때 Submission이 생성된다")
    void 제출물_생성_성공() {
        //given&when
        Submission submission = aSubmission().build();

        //then
        assertThat(submission).isNotNull();
        assertThat(submission.isLate()).isFalse();
    }

    @ParameterizedTest(name = "{0}이 null이면 생성에 실패한다")
    @MethodSource("nullScenarios")
    void create메서드_인자_널_실패(String fieldName, Submission.SubmissionBuilder badBuilder) {
        //when&then
        assertThatThrownBy(() -> badBuilder.build())
                .isInstanceOf(SubmissionException.class)
                .extracting("errorCode")
                .isEqualTo(SubmissionErrorCode.CREATE_ARGUMENT_MISSING);
    }

    private static Stream<Arguments> nullScenarios() {
        return Stream.of(
                Arguments.of("과제 ID", SubmissionFixture.aSubmission().assignmentId(null)),
                Arguments.of("제출자 ID", SubmissionFixture.aSubmission().submitterId(null)),
                Arguments.of("내용", SubmissionFixture.aSubmission().content(null)),
                Arguments.of("제출 시각", SubmissionFixture.aSubmission().currentDateTime(null)),
                Arguments.of("마감 기한", SubmissionFixture.aSubmission().assignmentDeadline(null))

        );
    }

    @Test
    @DisplayName("첨부파일이 2개 이상이면 예외가 발생한다")
    void 첨부파일_2개_이상_실패() {
        //given
        List<SubmissionAttachment> twoAttachments = List.of(
                new SubmissionAttachment("http://url1"),
                new SubmissionAttachment("http://url2")
        );

        //when&then
        assertThatThrownBy(() -> aSubmission()
                .attachments(twoAttachments)
                .build())
                .isInstanceOf(SubmissionException.class)
                .extracting("errorCode")
                .isEqualTo(SubmissionErrorCode.ATTACHMENT_LIMIT_EXCEEDED);
    }

    @Test
    @DisplayName("마감 기한을 넘기면 '지각' 상태가 되고 이벤트가 발행된다")
    void 이벤트_발행_지각_성공() {
        //given
        LocalDateTime deadline = LocalDateTime.of(2026, 1, 1, 12, 0);
        LocalDateTime submitTime = LocalDateTime.of(2026, 1, 1, 12, 1);

        //when
        Submission submission = aSubmission()
                .currentDateTime(submitTime)
                .assignmentDeadline(deadline)
                .build();

        //then
        assertThat(submission.isLate()).isTrue();

        Collection<Object> events = submission.domainEvents();
        assertThat(events).hasSize(1);

        Object event = events.iterator().next();
        assertThat(event).isInstanceOf(SubmissionCreatedEvent.class);
        assertThat(((SubmissionCreatedEvent) event).isLate()).isTrue();
    }

    @Test
    @DisplayName("마감 기한 내 제출이면 '지각 아님' 상태가 되고 이벤트가 발행된다")
    void 이벤트_발행_성공() {
        // given
        LocalDateTime deadline = LocalDateTime.of(2026, 1, 1, 12, 0);
        LocalDateTime submitTime = LocalDateTime.of(2026, 1, 1, 12, 0);

        //when
        Submission submission = aSubmission()
                .currentDateTime(submitTime)
                .assignmentDeadline(deadline)
                .build();

        //then
        assertThat(submission.isLate()).isFalse();

        Collection<Object> events = submission.domainEvents();
        assertThat(events).hasSize(1);
        assertThat(((SubmissionCreatedEvent) events.iterator().next()).isLate()).isFalse();
    }
}

