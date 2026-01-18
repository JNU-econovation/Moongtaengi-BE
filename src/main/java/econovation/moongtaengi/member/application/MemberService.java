package econovation.moongtaengi.member.application;

import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.MemberRepository;
import econovation.moongtaengi.member.domain.Nickname;
import econovation.moongtaengi.member.domain.NicknameFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final NicknameFactory nicknameFactory;

    public void checkNicknameAvailability(String nickname) {
        nicknameFactory.createNickname(nickname);
    }

    @Transactional
    public void completeRegistration(Long memberId, String nicknameValue) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Nickname nickname = nicknameFactory.createNickname(nicknameValue);

        member.completeRegistration(nickname);

        memberRepository.save(member);

        log.info("최종 회원가입 완료 - memberId: {}, nickname: {}",
                memberId, nickname.getValue());
    }
}
