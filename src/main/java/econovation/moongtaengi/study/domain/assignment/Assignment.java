package econovation.moongtaengi.study.domain.assignment;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assignments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "process_id", nullable = false)
    private Long processId;

    @Column(name = "assignee_id", nullable = false)
    private Long assigneeId;

    @Embedded
    private AssignmentContent content;

    @Embedded
    private AssignmentDeadline deadline;

    private Assignment(Long processId, Long assigneeId, AssignmentContent content, AssignmentDeadline deadline) {
        this.processId = processId;
        this.assigneeId = assigneeId;
        this.content = content;
        this.deadline = deadline;
    }

    @Builder
    public static Assignment create(Long processId, Long assigneeId, AssignmentContent content, AssignmentDeadline deadline) {
        validate(processId, assigneeId, content, deadline);

        return new Assignment(processId, assigneeId, content, deadline);
    }

    private static void validate(Long processId, Long assigneeId, AssignmentContent content, AssignmentDeadline deadline) {
        if (processId == null || assigneeId == null || content == null || deadline == null) {
            throw new AssignmentException(AssignmentErrorCode.INVALID_ASSIGNMENT_INFO);
        }
    }
}
