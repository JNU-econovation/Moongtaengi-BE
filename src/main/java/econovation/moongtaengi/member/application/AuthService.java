package econovation.moongtaengi.member.application;

import econovation.moongtaengi.global.security.JwtTokenProvider;
import econovation.moongtaengi.member.domain.QuestType;
import econovation.moongtaengi.member.infra.oauth.KakaoOAuthClient;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final ExperienceService experienceService;

    @Transactional
    public KakaoLoginResponse loginWithKakao(String code) {
        String kakaoAccessToken = kakaoOAuthClient.getAccessToken(code);

        String kakaoId = kakaoOAuthClient.getKakaoId(kakaoAccessToken);

        Member member = memberRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> createTemporaryMember(kakaoId));

        String accessToken = jwtTokenProvider.createAccessToken(member.getId());

        if (member.isActive()) {
            experienceService.completeQuest(member.getId(), QuestType.LOGIN);
        }

        log.info("로그인 성공 - memberId: {}, needsInfo: {}",
                member.getId(), member.isTemporary());

        return new KakaoLoginResponse(
                accessToken,
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
            String accessToken,
            boolean needsAdditionalInfo  // true면 추가정보 입력 페이지로 이동
    ) {}
}
