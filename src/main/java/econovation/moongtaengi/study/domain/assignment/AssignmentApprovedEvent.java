package econovation.moongtaengi.study.domain.assignment;

/**
 * 과제 승인 이벤트
 *
 * @param assignmentId 승인된 과제 ID
 * @param assigneeId   과제 제출자 ID
 */
public record AssignmentApprovedEvent(
        Long assignmentId,
        Long assigneeId
) {
}