package econovation.moongtaengi.study.domain.comment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentContent {
    public static final int MAX_LENGTH = 500;

    @Column(name = "content", nullable = false, length = MAX_LENGTH)
    private String value;

    public CommentContent(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new CommentException(CommentErrorCode.INVALID_COMMENT_INFO);
        }

        if (value.length() > MAX_LENGTH) {
            throw new CommentException(CommentErrorCode.CONTENT_TOO_LONG, MAX_LENGTH);
        }
    }
}
