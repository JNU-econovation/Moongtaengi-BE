package econovation.moongtaengi.member.application;

import econovation.moongtaengi.member.domain.NicknameFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final NicknameFactory nicknameFactory;

    public void checkNicknameAvailability(String nickname) {
        nicknameFactory.createNickname(nickname);
    }
}
