package econovation.moongtaengi.study.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import econovation.moongtaengi.study.application.comment.UpdateCommentCommand;
import econovation.moongtaengi.study.application.comment.UpdateCommentService;
import econovation.moongtaengi.study.domain.comment.Comment;
import econovation.moongtaengi.study.domain.comment.CommentContent;
import econovation.moongtaengi.study.domain.comment.CommentErrorCode;
import econovation.moongtaengi.study.domain.comment.CommentException;
import econovation.moongtaengi.study.domain.comment.CommentRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UpdateCommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private UpdateCommentService updateCommentService;

    @Test
    @DisplayName("본인의 댓글 내용을 정상적으로 수정한다.")
    void 댓글_수정_성공() {
        //given
        Long commentId = 1L;
        Long ownerId = 100L;
        String oldContent = "기존 내용";
        String newContent = "수정된 내용";

        Comment comment = Comment.create(
                999L,
                ownerId,
                new CommentContent(oldContent)
        );

        given(commentRepository.findById(commentId))
                .willReturn(Optional.of(comment));

        UpdateCommentCommand command = new UpdateCommentCommand(commentId, ownerId, newContent);

        // when
        updateCommentService.updateComment(command);

        //then
        assertThat(comment.getContent().getValue()).isEqualTo(newContent);
    }


    @Test
    @DisplayName("존재하지 않는 댓글 ID로 요청 시 예외가 발생한다.")
    void 댓글_수정_댓글_없음_실패() {
        //given
        Long invalidCommentId = 9999L;
        Long memberId = 1L;

        UpdateCommentCommand command = new UpdateCommentCommand(invalidCommentId, memberId, "테스트");

        given(commentRepository.findById(invalidCommentId))
                .willReturn(Optional.empty());

        //when&then
        assertThatThrownBy(() -> updateCommentService.updateComment(command))
                .isInstanceOf(CommentException.class)
                .extracting("errorCode")
                .isEqualTo(CommentErrorCode.COMMENT_NOT_FOUND);
    }
}
