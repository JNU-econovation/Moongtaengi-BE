package econovation.moongtaengi.study.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

import econovation.moongtaengi.study.application.comment.CreateCommentCommand;
import econovation.moongtaengi.study.application.comment.CreateCommentService;
import econovation.moongtaengi.study.domain.comment.Comment;
import econovation.moongtaengi.study.domain.comment.CommentMemberValidator;
import econovation.moongtaengi.study.domain.comment.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateCommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMemberValidator commentMemberValidator;

    @Captor
    private ArgumentCaptor<Comment> commentCaptor;

    @InjectMocks
    private CreateCommentService createCommentService;

    @Test
    @DisplayName("Command를 받아 검증 후 댓글을 생성한다.")
    void 댓글_생성_성공() {
        //given
        Long memberId = 1L;
        Long submissionId = 100L;
        String content = "테스트 댓글";

        CreateCommentCommand command = new CreateCommentCommand(
                memberId,
                submissionId,
                content
        );

        willDoNothing().given(commentMemberValidator)
                .validate(memberId, submissionId);

        //when
        createCommentService.createComment(command);

        //then
        verify(commentMemberValidator).validate(memberId, submissionId);
        verify(commentRepository).save(commentCaptor.capture());

        Comment savedComment = commentCaptor.getValue();

        assertThat(savedComment.getSubmissionId()).isEqualTo(submissionId);
        assertThat(savedComment.getMemberId()).isEqualTo(memberId);
        assertThat(savedComment.getContent().getValue()).isEqualTo(content);
    }

}
