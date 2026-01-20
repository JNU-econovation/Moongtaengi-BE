package econovation.moongtaengi.notification.application;

import econovation.moongtaengi.gamification.domain.DailyQuest;
import econovation.moongtaengi.gamification.domain.DailyQuestRepository;
import econovation.moongtaengi.gamification.domain.QuestType;
import econovation.moongtaengi.notification.api.dto.DailyQuestResponse;
import econovation.moongtaengi.notification.api.dto.NotificationResponse;
import econovation.moongtaengi.notification.api.dto.OnboardingMissionResponse;
import econovation.moongtaengi.notification.domain.Notification;
import econovation.moongtaengi.notification.domain.NotificationRepository;
import econovation.moongtaengi.notification.domain.NotificationType;
import econovation.moongtaengi.onboarding.application.OnboardingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final OnboardingService onboardingService;
    private final DailyQuestRepository dailyQuestRepository;

    public List<NotificationResponse> getNotifications(Long memberId) {
        List<Notification> notifications = notificationRepository.findAll().stream()
                .filter(n -> n.getMemberId().equals(memberId))
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
        return notifications.stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
    }

    public OnboardingMissionResponse getOnboardingMission(Long memberId) {
        return onboardingService.getOnboardingMission(memberId);
    }

    public List<DailyQuestResponse> getDailyQuests(Long memberId) {
        List<QuestType> displayTypes = List.of(
                QuestType.COMMENT,
                QuestType.SUBMIT_ASSIGNMENT,
                QuestType.REACTION
        );

        return displayTypes.stream()
                .map(type -> dailyQuestRepository.findByMemberIdAndQuestType(memberId, type)
                        .map(DailyQuestResponse::from)
                        .orElse(DailyQuestResponse.builder()
                                .message(type.getDisplayName())
                                .current(0)
                                .max(type.getDailyLimit())
                                .build()))
                .toList();
    }

    @Transactional
    public void deleteNotification(Long memberId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        if (!notification.getMemberId().equals(memberId)) {
            throw new SecurityException("You do not have permission to delete this notification");
        }

        notificationRepository.deleteById(notificationId);
    }

    @Transactional
    public void createNotification(Long memberId, NotificationType type, String message, String referenceType) {
        Notification notification = Notification.create(memberId, type, message, referenceType);
        notificationRepository.save(notification);
    }
}
