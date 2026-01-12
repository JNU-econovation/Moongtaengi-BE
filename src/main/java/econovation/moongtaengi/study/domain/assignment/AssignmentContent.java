package econovation.moongtaengi.study.domain.assignment;

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
public class AssignmentContent {
    private static final int MAX_LENGTH = 300;

    @Column(name = "content", nullable = false, length = MAX_LENGTH)
    private String value;

    public AssignmentContent(String value) {
        validate(value);
        this.value = value.trim();
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new AssignmentException(AssignmentErrorCode.INVALID_ASSIGNMENT_INFO);
        }

        if (value.trim().length() > MAX_LENGTH) {
            throw new AssignmentException(AssignmentErrorCode.INVALID_ASSIGNMENT_INFO);
        }
    }
}
