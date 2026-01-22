package econovation.moongtaengi.study.domain.submission;

import static econovation.moongtaengi.study.domain.submission.SubmissionFixture.aSubmission;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SubmissionTest {
    @Test
    @DisplayName("첨부파일이 포함된 Submission이 정상 생성되고, Optional로 조회된다")
    void 제출물_생성_성공_첨부파일_있음() {
        //given
        SubmissionAttachment attachment = SubmissionAttachment.of("과제.pdf", "https://url.com");

        //when
        Submission submission = aSubmission()
                .attachment(attachment)
                .build();

        //then
        assertThat(submission).isNotNull();
        assertThat(submission.getAttachment()).isPresent();
        assertThat(submission.getAttachment().get().getName()).isEqualTo("과제.pdf");
        assertThat(submission.isLate()).isFalse();
    }

    @Test
    @DisplayName("첨부파일이 없는 Submission도 정상 생성되며, 빈 Optional이 조회된다")
    void 제출물_생성_성공_첨부파일_없음() {
        //given&when
        Submission submission = aSubmission()
                .attachment(null)
                .build();

        //then
        assertThat(submission).isNotNull();
        assertThat(submission.getAttachment()).isEmpty();
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

    @Test
    @DisplayName("제출물의 내용과 첨부파일을 수정할 수 있다")
    void 제출물_수정_성공() {
        //given
        Long submitterId = 1L;
        Submission submission = aSubmission()
                .submitterId(submitterId)
                .build();

        SubmissionContent newContent = new SubmissionContent("수정된 내용");
        SubmissionAttachment newAttachment = SubmissionAttachment.of("new.pdf", "https://new-url.com");

        //when
        submission.update(submitterId, newContent, newAttachment);

        //then
        assertThat(submission.getContent()).isEqualTo(newContent);
        assertThat(submission.getAttachment()).isPresent();
        assertThat(submission.getAttachment().get().getName()).isEqualTo("new.pdf");
        assertThat(submission.getAttachment().get().getUrl()).isEqualTo("https://new-url.com");
    }

    @Test
    @DisplayName("첨부파일을 null로 업데이트하면 첨부파일이 삭제된다")
    void 제출물_수정_첨부파일_삭제() {
        //given
        Long submitterId = 1L;
        Submission submission = aSubmission()
                .submitterId(submitterId)
                .build();

        SubmissionContent newContent = new SubmissionContent("첨부파일 삭제함");

        //when
        submission.update(submitterId, newContent, null);

        //then
        assertThat(submission.getContent()).isEqualTo(newContent);
        assertThat(submission.getAttachment()).isEmpty();
    }

    @Test
    @DisplayName("수정 시 내용은 필수이므로 null이 들어오면 예외가 발생한다")
    void 제출물_수정_실패_내용누락() {
        //given
        Long submitterId = 1L;
        Submission submission = aSubmission()
                .submitterId(submitterId)
                .build();

        //when&then
        assertThatThrownBy(() -> submission.update(submitterId,  null, null))
                .isInstanceOf(SubmissionException.class)
                .extracting("errorCode")
                .isEqualTo(SubmissionErrorCode.INVALID_SUBMISSION_INFO);
    }

    @Test
    @DisplayName("작성자가 아닌 사람이 수정을 시도하면 예외가 발생한다")
    void 제출물_수정_실패_작성자_불일치() {
        //given
        Long submitterId = 1L;
        Long otherUserId = 999L;

        Submission submission = aSubmission()
                .submitterId(submitterId)
                .build();

        SubmissionContent newContent = new SubmissionContent("다른 사람이지롱~");

        //when&then
        assertThatThrownBy(() ->
                submission.update(otherUserId, newContent, null)
        )
                .isInstanceOf(SubmissionException.class)
                .extracting("errorCode")
                .isEqualTo(SubmissionErrorCode.NOT_SUBMISSION_OWNER);
    }
}

