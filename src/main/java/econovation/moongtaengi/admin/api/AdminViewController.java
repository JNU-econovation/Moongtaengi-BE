package econovation.moongtaengi.admin.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 관리자 페이지 View Controller
 */
@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminViewController {

    /**
     * 관리자 로그인 페이지
     * GET /admin/login
     */
    @GetMapping("/login")
    public String loginPage() {
        log.info("관리자 로그인 페이지 요청");
        return "admin/login";
    }
}
