package econovation.moongtaengi.study.domain.submission;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class SubmissionContent {
    public static final int MAX_LENGTH = 500000;

    @Column(name = "content", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String value;

    public SubmissionContent(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new SubmissionException(SubmissionErrorCode.INVALID_SUBMISSION_INFO);
        }

        if (value.length() > MAX_LENGTH) {
            throw new SubmissionException(SubmissionErrorCode.CONTENT_TOO_LONG, MAX_LENGTH);
        }
    }
}
