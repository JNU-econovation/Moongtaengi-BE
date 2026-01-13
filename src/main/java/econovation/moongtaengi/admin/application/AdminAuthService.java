package econovation.moongtaengi.admin.application;

import econovation.moongtaengi.admin.api.dto.AdminLoginRequest;
import econovation.moongtaengi.admin.api.dto.AdminLoginResponse;
import econovation.moongtaengi.admin.domain.AdminErrorCode;
import econovation.moongtaengi.admin.domain.AdminException;
import econovation.moongtaengi.global.security.JwtTokenProvider;
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
public class AdminAuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 관리자 로그인
     */
    public AdminLoginResponse login(AdminLoginRequest request) {
        log.info("관리자 로그인 시도 - kakaoId: {}", request.kakaoId());

        // 1. 관리자 계정 조회
        Member admin = memberRepository.findByKakaoId(request.kakaoId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 관리자 - kakaoId: {}", request.kakaoId());
                    return new AdminException(AdminErrorCode.ADMIN_NOT_FOUND);
                });

        // 2. 관리자 권한 확인
        if (!admin.isAdmin()) {
            log.warn("관리자 권한 없음 - kakaoId: {}, role: {}",
                    request.kakaoId(), admin.getRole());
            throw new AdminException(AdminErrorCode.UNAUTHORIZED_ADMIN_ACCESS);
        }

        // 3. JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(admin.getId());

        log.info("✅ 관리자 로그인 성공 - adminId: {}, nickname: {}",
                admin.getId(), admin.getNickname().getValue());

        return new AdminLoginResponse(
                accessToken,
                admin.getNickname().getValue(),
                admin.getRole().name()
        );
    }
}
