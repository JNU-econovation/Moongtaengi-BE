package econovation.moongtaengi.admin.api;

import econovation.moongtaengi.admin.api.dto.AdminLoginRequest;
import econovation.moongtaengi.admin.api.dto.AdminLoginResponse;
import econovation.moongtaengi.admin.application.AdminAuthService;
import econovation.moongtaengi.admin.application.AdminMemberService;
import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 관리자 페이지 View Controller
 */
@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminViewController {

    private final AdminAuthService adminAuthService;
    private final AdminMemberService adminMemberService;

    /**
     * 로그인 페이지
     * GET /admin/login
     */
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        log.info("관리자 로그인 페이지 요청");

        // 이미 로그인되어 있으면 대시보드로
        if (isLoggedIn(session)) {
            return "redirect:/admin/dashboard";
        }

        return "admin/login";
    }

    @PostMapping("/do-login")
    public String doLogin(
            @RequestParam String kakaoId,
            HttpSession session,
            Model model
    ) {
        log.info("관리자 로그인 처리 - kakaoId: {}", kakaoId);

        try {
            // 1. API로 로그인 시도
            AdminLoginResponse response = adminAuthService.login(
                    new AdminLoginRequest(kakaoId)
            );

            // 2. ✅ Spring Security 인증 객체 생성
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            kakaoId,  // principal
                            null,     // credentials
                            Collections.singletonList(authority)  // authorities
                    );

            // 3. ✅ SecurityContext에 인증 정보 설정
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);

            // 4. ✅ 세션에 SecurityContext 저장
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    securityContext
            );

            // 5. 커스텀 세션 속성 저장
            session.setAttribute("ADMIN_LOGGED_IN", true);
            session.setAttribute("ADMIN_TOKEN", response.accessToken());
            session.setAttribute("ADMIN_NICKNAME", response.nickname());
            session.setAttribute("ADMIN_ROLE", response.role());

            // 6. Model에 데이터 추가 (localStorage용)
            model.addAttribute("accessToken", response.accessToken());
            model.addAttribute("nickname", response.nickname());

            log.info("✅ 관리자 로그인 성공 - nickname: {}", response.nickname());

            // 7. 중간 페이지로 이동
            return "admin/login-success";

        } catch (Exception e) {
            log.error("관리자 로그인 실패 - kakaoId: {}, error: {}", kakaoId, e.getMessage());
            model.addAttribute("error", "로그인에 실패했습니다. 카카오 ID를 확인하세요.");
            return "admin/login";
        }
    }

    /**
     * 로그아웃
     * GET /admin/logout
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.info("관리자 로그아웃");
        session.invalidate();
        return "redirect:/admin/login";
    }

    /**
     * 대시보드
     * GET /admin/dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        log.info("관리자 대시보드 페이지 요청");

        // 세션 체크
        if (!isLoggedIn(session)) {
            log.warn("로그인되지 않은 상태로 대시보드 접근 시도");
            return "redirect:/admin/login";
        }

        // 관리자 권한 체크
        if (!isAdmin(session)) {
            log.warn("관리자 권한 없음");
            session.invalidate();
            return "redirect:/admin/login";
        }

        long memberCount = adminMemberService.getMemberCount();
        model.addAttribute("memberCount", memberCount);
        model.addAttribute("adminNickname", session.getAttribute("ADMIN_NICKNAME"));

        return "admin/dashboard";
    }

    /**
     * 회원 관리 페이지
     * GET /admin/members
     */
    @GetMapping("/members")
    public String memberList(HttpSession session) {
        log.info("회원 관리 페이지 요청");

        // 세션 체크
        if (!isLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        // 관리자 권한 체크
        if (!isAdmin(session)) {
            session.invalidate();
            return "redirect:/admin/login";
        }

        return "admin/member/list";
    }

    /**
     * ✅ 로그인 여부 확인
     */
    private boolean isLoggedIn(HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("ADMIN_LOGGED_IN");
        return loggedIn != null && loggedIn;
    }

    /**
     * ✅ 관리자 권한 확인
     */
    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("ADMIN_ROLE");
        return "ADMIN".equals(role);
    }
}
