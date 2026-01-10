package econovation.moongtaengi.admin.api;

import econovation.moongtaengi.admin.api.dto.AdminLoginRequest;
import econovation.moongtaengi.admin.api.dto.AdminLoginResponse;
import econovation.moongtaengi.admin.application.AdminAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 관리자 인증 API
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    /**
     * 관리자 로그인
     * POST /api/admin/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(
            @Valid @RequestBody AdminLoginRequest request
    ) {
        log.info("관리자 로그인 API 호출 - kakaoId: {}", request.kakaoId());

        AdminLoginResponse response = adminAuthService.login(request);

        log.info("✅ 관리자 로그인 API 완료");

        return ResponseEntity.ok(response);
    }
}
