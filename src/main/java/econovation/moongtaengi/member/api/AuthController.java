package econovation.moongtaengi.member.api;

import econovation.moongtaengi.global.annotation.LoginMemberId;
import econovation.moongtaengi.member.api.dto.MemberInfoResponse;
import econovation.moongtaengi.member.application.AuthService;
import econovation.moongtaengi.member.application.AuthService.KakaoLoginResponse;
import econovation.moongtaengi.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;

    @Value("${oauth.kakao.frontend-redirect-uri}")
    private String frontendRedirectUri;

    @GetMapping("/kakao/callback")
    public RedirectView kakaoCallback(@RequestParam String code) {
        KakaoLoginResponse response = authService.loginWithKakao(code);

        String redirectUrl = buildRedirectUrl(response);

        return new RedirectView(redirectUrl);
    }

    /**
     * 현재 로그인한 회원의 정보 조회
     * @param memberId JWT에서 추출한 로그인 회원 ID
     * @return 회원 정보 (ID, 닉네임)
     */
    @GetMapping("/me")
    public MemberInfoResponse getMyInfo(@LoginMemberId Long memberId) {
        log.info("회원 본인 정보 조회 요청 - memberId: {}", memberId);
        return memberService.getMyInfo(memberId);
    }

    private String buildRedirectUrl(KakaoLoginResponse response) {

        String path = response.needsAdditionalInfo()
                ? "/signup"
                : "/auth/callback";

        return UriComponentsBuilder
                .fromUriString(frontendRedirectUri + path)
                .queryParam("token", response.accessToken())
                .build()
                .toUriString();
    }
}
