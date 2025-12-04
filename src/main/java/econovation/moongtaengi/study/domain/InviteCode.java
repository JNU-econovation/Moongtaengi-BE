package econovation.moongtaengi.study.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InviteCode {
    private static final int LENGTH = 8;

    @Column(name = "invite_code", nullable = false, unique = true)
    private String value;

    public InviteCode(String value) {
        validate(value);
        this.value = value;
    }

    public static InviteCode generate() {
        String randomCode = UUID.randomUUID().toString()
                .replaceAll("-", "")
                .substring(0, LENGTH);

        return new InviteCode(randomCode);
    }

    private void validate(String value) {
        if (value == null) {
            throw new IllegalStateException("초대코드는 필수입니다.");
        }

        if (value.length() != LENGTH) {
            throw new IllegalStateException("초대코드는 " + LENGTH + "글자입니다.");
        }
    }
}
