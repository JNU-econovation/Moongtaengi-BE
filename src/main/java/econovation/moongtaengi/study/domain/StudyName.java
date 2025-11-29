package econovation.moongtaengi.study.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyName {
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 10;
    private static final Pattern PATTERN = Pattern.compile("^[가-힣a-zA-Z0-9\\s]+$");

    @Column(name = "name", nullable = false)
    private String value;

    public StudyName(String value) {
        String trimmed = (value != null) ? value.trim() : null;
        validate(trimmed);
        this.value = trimmed;
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("스터디 이름은 비어있을 수 없습니다.");
        }

        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("스터디 이름은 " + MIN_LENGTH + "자 이상 " + MAX_LENGTH + "자 이하여야 합니다.");
        }

        if (!PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("스터디 이름의 형식이 올바르지 않습니다.");
        }
    }
}
