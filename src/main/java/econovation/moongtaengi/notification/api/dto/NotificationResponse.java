package econovation.moongtaengi.notification.api.dto;

import econovation.moongtaengi.notification.domain.Notification;
import econovation.moongtaengi.notification.domain.NotificationType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationResponse(
        Long id,
        String message
) {
    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .build();
    }
}
