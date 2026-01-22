package econovation.moongtaengi.home.api;

import econovation.moongtaengi.global.annotation.LoginMemberId;
import econovation.moongtaengi.home.api.dto.HomeResponse;
import econovation.moongtaengi.home.application.HomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 메인 페이지 API 컨트롤러
 * 사용자 대시보드 정보 제공
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    /**
     * 메인 페이지 정보 조회
     * @param memberId 로그인한 회원 ID
     * @return 메인 페이지 응답
     */
    @GetMapping
    public ResponseEntity<HomeResponse> getHomeInfo(@LoginMemberId Long memberId) {
        log.info("메인 페이지 조회 API 호출 - memberId: {}", memberId);

        HomeResponse response = homeService.getHomeInfo(memberId);
        return ResponseEntity.ok(response);
    }
}
