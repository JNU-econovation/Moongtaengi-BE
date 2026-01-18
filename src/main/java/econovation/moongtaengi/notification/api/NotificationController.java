package econovation.moongtaengi.notification.api;

import econovation.moongtaengi.global.annotation.LoginMemberId;
import econovation.moongtaengi.notification.api.dto.DailyQuestResponse;
import econovation.moongtaengi.notification.api.dto.NotificationResponse;
import econovation.moongtaengi.notification.api.dto.OnboardingMissionResponse;
import econovation.moongtaengi.notification.application.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 알림 API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 알림 목록 조회
     * - 온보딩 미션 (상단 고정, 1개)
     * - 일일 퀘스트 (고정 3개)
     * - 기본 알림 (DB에서 전체 조회)
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getNotifications(@LoginMemberId Long memberId) {
        log.info("알림 목록 조회 API 호출 - memberId: {}", memberId);

        OnboardingMissionResponse onboarding = notificationService.getOnboardingMission(memberId);
        List<DailyQuestResponse> dailyQuests = notificationService.getDailyQuests(memberId);
        List<NotificationResponse> notifications = notificationService.getNotifications(memberId);

        Map<String, Object> response = Map.of(
                "onboarding", onboarding,
                "dailyQuests", dailyQuests,
                "notifications", notifications
        );

        return ResponseEntity.ok(response);
    }

    /**
     * 알림 삭제 (읽음 처리)
     * 소유권 검증 후 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(
            @LoginMemberId Long memberId,
            @PathVariable Long id) {
        log.info("알림 삭제 API 호출 - memberId: {}, notificationId: {}", memberId, id);
        notificationService.deleteNotification(memberId, id);
        return ResponseEntity.noContent().build();
    }
}