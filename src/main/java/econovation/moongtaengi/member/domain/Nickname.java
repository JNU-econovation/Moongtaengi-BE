package econovation.moongtaengi.member.domain;


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
public class Nickname {
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 7;
    private static final Pattern PATTERN = Pattern.compile("^[가-힣0-9]+$");

    @Column(name = "nickname", unique = true)
    private String value;

    public Nickname(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new NicknameException(MemberErrorCode.NICKNAME_NOT_BLANK);
        }

        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new NicknameException(MemberErrorCode.NICKNAME_LENGTH_INVALID, MIN_LENGTH, MAX_LENGTH);
        }

        if (!PATTERN.matcher(value).matches()) {
            throw new NicknameException(MemberErrorCode.NICKNAME_INVALID_PATTERN);
        }
    }
}
