package econovation.moongtaengi.member.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NicknameFactory {
    private final BadWordValidator badWordValidator;
    private final MemberRepository memberRepository;

    public Nickname createNickname(String rawNickname) {
        Nickname nickname = new Nickname(rawNickname);

        validateBadWord(nickname);

        validateDuplication(nickname);

        return nickname;
    }

    private void validateBadWord(Nickname nickname) {
        if (badWordValidator.containsBadWord(nickname.getValue())) {
            throw new IllegalArgumentException("비속어가 포함되어 있습니다.");
        }
    }

    private void validateDuplication(Nickname nickname) {
        if (memberRepository.existsByNicknameValue(nickname.getValue())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
    }
}
