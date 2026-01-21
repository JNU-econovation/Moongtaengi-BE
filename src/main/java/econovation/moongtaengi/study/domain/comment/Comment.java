package econovation.moongtaengi.study.domain.comment;

import econovation.moongtaengi.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class Comment extends BaseEntity {
    @Column(nullable = false)
    private Long submissionId;

    @Column(nullable = false)
    private Long memberId;

    @Embedded
    private CommentContent content;

    private Comment(Long submissionId, Long memberId, CommentContent content) {
        this.submissionId = submissionId;
        this.memberId = memberId;
        this.content = content;
    }

    public static Comment create(Long submissionId, Long memberId, CommentContent content) {
        validateCreation(submissionId, memberId, content);
        Comment comment = new Comment(submissionId, memberId, content);
        // 이벤트 등록은 ID가 할당된 후에 발생해야 하므로, 서비스 레이어에서 save 후 처리
        return comment;
    }

    /**
     * 댓글 생성 후 이벤트 등록 (save 후 호출)
     */
    public void publishCreatedEvent() {
        registerEvent(new CommentCreatedEvent(this.getId(), this.submissionId, this.memberId));
    }

    public void updateContent(Long requestMemberId, CommentContent content) {
        validateOwner(requestMemberId);
        validateContent(content);
        this.content = content;
    }

    private static void validateCreation(Long submissionId, Long memberId, CommentContent content) {
        if (submissionId == null || memberId == null || content == null) {
            throw new CommentException(CommentErrorCode.INVALID_COMMENT_INFO);
        }
    }

    public void validateOwner(Long requestMemberId) {
        if (!this.memberId.equals(requestMemberId)) {
            throw new CommentException(CommentErrorCode.NOT_COMMENT_OWNER);
        }
    }

    private void validateContent(CommentContent content) {
        if (content == null) {
            throw new CommentException(CommentErrorCode.INVALID_COMMENT_INFO);
        }
    }
}
