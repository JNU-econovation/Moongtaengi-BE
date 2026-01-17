package econovation.moongtaengi.study.domain.event;

public record StudyJoinedEvent(
        Long studyId,
        Long memberId
) {
}