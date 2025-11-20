package econovation.moongtaengi.member.api;

import econovation.moongtaengi.member.application.AuthService;
import econovation.moongtaengi.member.application.AuthService.KakaoLoginResponse;
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

    @Value("${oauth.kakao.frontend-redirect-uri}")
    private String frontendRedirectUri;

    @GetMapping("/kakao/callback")
    public RedirectView kakaoCallback(@RequestParam String code) {
        KakaoLoginResponse response = authService.loginWithKakao(code);

        String redirectUrl = buildRedirectUrl(response);

        return new RedirectView(redirectUrl);
    }

    private String buildRedirectUrl(KakaoLoginResponse response) {
        // 임시 회원
        if (response.needsAdditionalInfo()) {
            return UriComponentsBuilder
                    .fromUriString(frontendRedirectUri + "/signup/additional-info")
                    .queryParam("memberId", response.memberId())
                    .build()
                    .toUriString();
        }

        // 기존 회원
        return UriComponentsBuilder
                .fromUriString(frontendRedirectUri + "/")
                .queryParam("memberId", response.memberId())
                .build()
                .toUriString();
    }
}
