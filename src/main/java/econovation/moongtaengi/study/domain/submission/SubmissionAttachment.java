package econovation.moongtaengi.study.domain.submission;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class SubmissionAttachment {
    private static final String URL_REGEX = "^https?://\\S+$";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    @Column(name = "file_url", nullable = false)
    private String url;

    public SubmissionAttachment(String url) {
        validate(url);
        this.url = url;
    }

    private void validate(String url) {
        if (url == null || url.isBlank()) {
            throw new SubmissionException(SubmissionErrorCode.INVALID_ATTACHMENT_URL);
        }

        if (!URL_PATTERN.matcher(url).matches()) {
            throw new SubmissionException(SubmissionErrorCode.INVALID_ATTACHMENT_URL);
        }
    }
}
