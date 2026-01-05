package econovation.moongtaengi.study.domain.process;

import econovation.moongtaengi.study.domain.StudyErrorCode;
import econovation.moongtaengi.study.domain.StudyException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Memo {
    private static final int MAX_LENGTH = 50;

    @Column(name = "memo", nullable = false, length = 50)
    private String value;

    public Memo(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null) {
            throw new StudyException(StudyErrorCode.MEMO_NOT_NULL);
        }
        if (value.length() > MAX_LENGTH) {
            throw new StudyException(StudyErrorCode.MEMO_LENGTH_EXCEEDED, MAX_LENGTH);
        }
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }
}
