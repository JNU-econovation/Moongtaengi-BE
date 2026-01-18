package econovation.moongtaengi.notification.domain;

import econovation.moongtaengi.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 알림 엔티티
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Column(nullable = false)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String message;

    private String referenceType;

    private Notification(Long memberId, NotificationType type, String message, String referenceType) {
        this.memberId = memberId;
        this.type = type;
        this.message = message;
        this.referenceType = referenceType;
    }

    public static Notification create(Long memberId, NotificationType type, String message, String referenceType) {
        return new Notification(memberId, type, message, referenceType);
    }
}
