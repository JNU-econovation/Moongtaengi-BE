package econovation.moongtaengi.study.domain.comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class CommentTest {
    @Nested
    @DisplayName("댓글 내용(VO) 검증")
    class CommentContentTest {
        @Test
        @DisplayName("댓글 내용은 null이거나 빈 공백일 수 없다")
        void 댓글_널_공백_실패() {
            //when&then
            assertThatThrownBy(() -> new CommentContent(null))
                    .isInstanceOf(CommentException.class)
                    .extracting("errorCode")
                    .isEqualTo(CommentErrorCode.INVALID_COMMENT_INFO);

            assertThatThrownBy(() -> new CommentContent(""))
                    .isInstanceOf(CommentException.class)
                    .extracting("errorCode")
                    .isEqualTo(CommentErrorCode.INVALID_COMMENT_INFO);
        }

        @Test
        @DisplayName("댓글 내용은 500자를 넘길 수 없다")
        void 댓글_500자_실패() {
            //given
            String tooLongContent = "a".repeat(501);

            //when&then
            assertThatThrownBy(() -> new CommentContent(tooLongContent))
                    .isInstanceOf(CommentException.class)
                    .extracting("errorCode")
                    .isEqualTo(CommentErrorCode.CONTENT_TOO_LONG);
        }
    }

    @Nested
    @DisplayName("댓글 동작 검증")
    class CommentEntityTest {

        @Test
        @DisplayName("댓글 생성 성공")
        void 댓글_생성_성공() {
            //given
            Long submissionId = 1L;
            Long memberId = 100L;
            CommentContent content = new CommentContent("댓글 내용입니다.");

            //when
            Comment comment = Comment.create(submissionId, memberId, content);

            //then
            assertThat(comment.getSubmissionId()).isEqualTo(submissionId);
            assertThat(comment.getMemberId()).isEqualTo(memberId);
            assertThat(comment.getContent().getValue()).isEqualTo("댓글 내용입니다.");
        }

        @Test
        @DisplayName("필수 정보가 누락되면 생성할 수 없다")
        void 필수_정보_누락_댓글_생성_실패() {
            //given
            CommentContent content = new CommentContent("내용");

            //when&then
            assertThatThrownBy(() -> Comment.create(null, 1L, content))
                    .isInstanceOf(CommentException.class);

            assertThatThrownBy(() -> Comment.create(1L, null, content))
                    .isInstanceOf(CommentException.class);
        }
    }
}
