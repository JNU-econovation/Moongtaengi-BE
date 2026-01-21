package econovation.moongtaengi.study.domain.assignment;

import econovation.moongtaengi.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assignments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Assignment extends BaseEntity {
    @Column(name = "process_id", nullable = false)
    private Long processId;

    @Column(name = "assignee_id", nullable = false)
    private Long assigneeId;

    @Embedded
    private AssignmentDescription description;

    @Embedded
    private AssignmentDeadline deadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AssignmentStatus status;

    @Column(name = "is_late", nullable = false)
    private boolean isLate;

    private Assignment(Long processId, Long assigneeId, AssignmentDescription description, AssignmentDeadline deadline) {
        this.processId = processId;
        this.assigneeId = assigneeId;
        this.description = description;
        this.deadline = deadline;
        this.status = AssignmentStatus.WAITING;
        this.isLate = false;
    }

    @Builder
    public static Assignment create(Long processId, Long assigneeId, AssignmentDescription description, AssignmentDeadline deadline) {
        validate(processId, assigneeId, description, deadline);

        return new Assignment(processId, assigneeId, description, deadline);
    }

    public void markAsSubmitted(boolean isLate) {
        this.status = AssignmentStatus.SUBMITTED;
        this.isLate = isLate;
    }

    public void approve() {
        if (this.status != AssignmentStatus.SUBMITTED) {
            throw new AssignmentException(AssignmentErrorCode.CANNOT_APPROVE_NOT_SUBMITTED);
        }
        this.status = AssignmentStatus.APPROVED;
        registerEvent(new AssignmentApprovedEvent(this.getId(), this.assigneeId));
    }

    private static void validate(Long processId, Long assigneeId, AssignmentDescription description, AssignmentDeadline deadline) {
        if (processId == null || assigneeId == null || description == null || deadline == null) {
            throw new AssignmentException(AssignmentErrorCode.INVALID_ASSIGNMENT_INFO);
        }
    }
}
