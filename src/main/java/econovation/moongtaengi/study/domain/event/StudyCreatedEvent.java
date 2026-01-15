package econovation.moongtaengi.study.domain.event;

public record StudyCreatedEvent(
        Long studyId,
        Long memberId
) {
}
