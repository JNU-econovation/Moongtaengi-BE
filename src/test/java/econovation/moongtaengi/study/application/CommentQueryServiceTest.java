package econovation.moongtaengi.study.application;

import econovation.moongtaengi.collection.domain.CollectionType;
import econovation.moongtaengi.study.application.comment.CommentQueryService;
import econovation.moongtaengi.study.application.comment.CommentSummary;
import econovation.moongtaengi.study.domain.comment.CommentErrorCode;
import econovation.moongtaengi.study.domain.comment.CommentException;
import econovation.moongtaengi.study.domain.comment.CommentMemberValidator;
import econovation.moongtaengi.study.domain.comment.CommentRepository;
import econovation.moongtaengi.study.domain.comment.CommentSummaryRaw;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentQueryServiceTest {

    @InjectMocks
    private CommentQueryService commentQueryService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMemberValidator commentMemberValidator;

    @Test
    @DisplayName("스터디원이 아니면 댓글 목록 조회가 불가능하고 예외가 발생해야 한다.")
    void 스터디원이_아닌_경우_조회_실패() {
        //given
        Long submissionId = 100L;
        Long outsiderId = 999L;

        willThrow(new CommentException(CommentErrorCode.NO_PERMISSION))
                .given(commentMemberValidator)
                .validate(outsiderId, submissionId);

        //when&then
        assertThatThrownBy(() -> commentQueryService.getComments(submissionId, outsiderId))
                .isInstanceOf(CommentException.class)
                .extracting("errorCode")
                .isEqualTo(CommentErrorCode.NO_PERMISSION);

        verify(commentRepository, never()).findRawListBySubmissionId(any());
    }

    @Test
    @DisplayName("스터디원이면 검증을 통과하고 댓글 목록을 조회한다.")
    void 스터디원_검증_통과_및_조회_성공() {
        // given
        Long submissionId = 100L;
        Long memberId = 1L;

        CommentSummaryRaw raw = new CommentSummaryRaw(
                10L, "테스트 댓글", LocalDateTime.now(),
                1L, "지환", CollectionType.DEFAULT
        );

        willDoNothing().given(commentMemberValidator).validate(memberId, submissionId);

        given(commentRepository.findRawListBySubmissionId(submissionId))
                .willReturn(List.of(raw));

        //when
        List<CommentSummary> result = commentQueryService.getComments(submissionId, memberId);

        //then
        assertThat(result).hasSize(1);

        verify(commentMemberValidator).validate(memberId, submissionId);
        verify(commentRepository).findRawListBySubmissionId(submissionId);
    }
}