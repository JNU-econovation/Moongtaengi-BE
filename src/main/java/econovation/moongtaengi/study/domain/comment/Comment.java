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
        validate(submissionId, memberId, content);
        return new Comment(submissionId, memberId, content);
    }

    private static void validate(Long submissionId, Long memberId, CommentContent content) {
        if (submissionId == null || memberId == null || content == null) {
            throw new CommentException(CommentErrorCode.INVALID_COMMENT_INFO);
        }
    }
}
