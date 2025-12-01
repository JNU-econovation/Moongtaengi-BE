package econovation.moongtaengi.study.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyTopic {
    private static final int MAX_LENGTH = 50;

    @Column(name = "topic", nullable = false)
    private String value;

    public StudyTopic(String value) {
        String trimmed = (value != null) ? value.trim() : null;
        validate(trimmed);
        this.value = trimmed;
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("스터디 주제는 필수입니다.");
        }

        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("스터디 주제는 최대 " + MAX_LENGTH + "자까지만 가능합니다.");
        }
    }
}
