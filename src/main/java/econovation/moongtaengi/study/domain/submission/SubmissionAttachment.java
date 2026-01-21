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

    @Column(name = "file_name", nullable = false)
    private String name;

    @Column(name = "file_url", nullable = false)
    private String url;

    private SubmissionAttachment(String name, String url) {
        validate(name, url);
        this.name = name;
        this.url = url;
    }

    public static SubmissionAttachment of(String name, String url) {
        return new SubmissionAttachment(name, url);
    }

    private void validate(String name, String url) {
        if (name == null || name.isBlank()) {
            throw new SubmissionException(SubmissionErrorCode.INVALID_SUBMISSION_INFO);
        }


        if (url == null || url.isBlank()) {
            throw new SubmissionException(SubmissionErrorCode.INVALID_ATTACHMENT_URL);
        }

        if (!URL_PATTERN.matcher(url).matches()) {
            throw new SubmissionException(SubmissionErrorCode.INVALID_ATTACHMENT_URL);
        }
    }
}
