package econovation.moongtaengi.study.application.assignment;

public record UpdateDescriptionCommand(
        Long assignmentId,
        Long requesterId,
        String description
) {
    public static UpdateDescriptionCommand of(Long assignmentId, Long requesterId, String description) {
        return new UpdateDescriptionCommand(assignmentId, requesterId, description);
    }
}
