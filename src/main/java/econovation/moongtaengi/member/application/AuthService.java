package econovation.moongtaengi.member.application;

import econovation.moongtaengi.global.exception.KakaoAuthException;
import econovation.moongtaengi.global.oauth.KakaoOAuthClient;
import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final KakaoOAuthClient kakaoOAuthClient;

    @Transactional
    public KakaoLoginResponse loginWithKakao(String code) {
        String kakaoAccessToken = kakaoOAuthClient.getAccessToken(code);

        if (kakaoAccessToken == null) {
            throw new KakaoAuthException();
        }

        String kakaoId = kakaoOAuthClient.getKakaoId(kakaoAccessToken);
        if (kakaoId == null) {
            throw new KakaoAuthException();
        }

        Member member = memberRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> createTemporaryMember(kakaoId));

        return new KakaoLoginResponse(
                member.getId(),
                member.isTemporary()
        );
    }

    private Member createTemporaryMember(String kakaoId) {
        Member temporaryMember = Member.createTemporaryMember(kakaoId);
        Member savedMember = memberRepository.save(temporaryMember);
        log.info("임시 회원 생성 - memberId: {}, kakaoId: {}", savedMember.getId(), kakaoId);
        return savedMember;
    }

    public record KakaoLoginResponse(
            Long memberId,
            boolean needsAdditionalInfo  // true면 추가정보 입력 페이지로 이동
    ) {}
}
