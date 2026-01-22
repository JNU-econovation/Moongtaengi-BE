package econovation.moongtaengi.study.application;

import static econovation.moongtaengi.study.domain.submission.SubmissionFixture.DEFAULT_ATTACHMENT;
import static econovation.moongtaengi.study.domain.submission.SubmissionFixture.DEFAULT_CONTENT;
import static econovation.moongtaengi.study.domain.submission.SubmissionFixture.DEFAULT_SUBMITTER_ID;
import static econovation.moongtaengi.study.domain.submission.SubmissionFixture.aSubmission;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import econovation.moongtaengi.study.application.submission.UpdateSubmissionCommand;
import econovation.moongtaengi.study.application.submission.UpdateSubmissionService;
import econovation.moongtaengi.study.domain.submission.Submission;
import econovation.moongtaengi.study.domain.submission.SubmissionException;
import econovation.moongtaengi.study.domain.submission.SubmissionRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class UpdateSubmissionServiceTest {

    @Mock
    private SubmissionRepository submissionRepository;

    @InjectMocks
    private UpdateSubmissionService updateSubmissionService;

    @Test
    @DisplayName("내용과 첨부파일이 모두 주어지면 둘 다 수정된다")
    void 과제_수정_성공_전체수정() {
        //given
        Long submissionId = 1L;
        Submission submission = aSubmission().build();
        ReflectionTestUtils.setField(submission, "id", submissionId);

        UpdateSubmissionCommand command = new UpdateSubmissionCommand(
                submissionId,
                DEFAULT_SUBMITTER_ID,
                "완전 수정됨",
                "new.pdf",
                "http://new.com"
        );

        given(submissionRepository.findById(submissionId)).willReturn(Optional.of(submission));

        //when
        updateSubmissionService.updateSubmission(command);

        //then
        assertThat(submission.getContent().getValue()).isEqualTo("완전 수정됨");
        assertThat(submission.getAttachment().get().getName()).isEqualTo("new.pdf");
    }

    @Test
    @DisplayName("첨부파일 정보가 Null이면 기존 첨부파일이 유지된다 (삭제되지 않음)")
    void 과제_수정_성공_파일유지() {
        //given
        Long submissionId = 1L;
        Submission submission = aSubmission().build();
        ReflectionTestUtils.setField(submission, "id", submissionId);

        UpdateSubmissionCommand command = new UpdateSubmissionCommand(
                submissionId,
                DEFAULT_SUBMITTER_ID,
                "내용만 바꿈",
                null,
                null
        );

        given(submissionRepository.findById(submissionId)).willReturn(Optional.of(submission));

        //when
        updateSubmissionService.updateSubmission(command);

        //then
        assertThat(submission.getContent().getValue()).isEqualTo("내용만 바꿈");
        assertThat(submission.getAttachment()).isPresent();
        assertThat(submission.getAttachment().get().getName()).isEqualTo(DEFAULT_ATTACHMENT.getName());
    }

    @Test
    @DisplayName("내용이 Null이면 기존 내용이 유지된다")
    void 과제_수정_성공_내용유지() {
        //given
        Long submissionId = 1L;
        Submission submission = aSubmission().build();
        ReflectionTestUtils.setField(submission, "id", submissionId);

        UpdateSubmissionCommand command = new UpdateSubmissionCommand(
                submissionId,
                DEFAULT_SUBMITTER_ID,
                null,
                "only_file.pdf",
                "http://only.com"
        );

        given(submissionRepository.findById(submissionId)).willReturn(Optional.of(submission));

        //when
        updateSubmissionService.updateSubmission(command);

        //then
        assertThat(submission.getContent().getValue()).isEqualTo(DEFAULT_CONTENT.getValue());
        assertThat(submission.getAttachment().get().getName()).isEqualTo("only_file.pdf");
    }

    @Test
    @DisplayName("존재하지 않는 과제 ID면 예외가 발생한다")
    void 과제_수정_실패_미존재() {
        //given
        Long invalidId = 999L;
        UpdateSubmissionCommand command = new UpdateSubmissionCommand(
                invalidId, DEFAULT_SUBMITTER_ID, "content", "file", "url"
        );

        given(submissionRepository.findById(invalidId)).willReturn(Optional.empty());

        //when&then
        assertThatThrownBy(() -> updateSubmissionService.updateSubmission(command))
                .isInstanceOf(SubmissionException.class);
    }
}