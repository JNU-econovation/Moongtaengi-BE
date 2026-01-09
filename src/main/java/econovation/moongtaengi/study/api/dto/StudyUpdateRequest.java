package econovation.moongtaengi.study.api.dto;

import econovation.moongtaengi.study.application.UpdateStudyCommand;
import java.time.LocalDate;

public record StudyUpdateRequest(
        String name,
        String topic,
        LocalDate startDate,
        LocalDate endDate
) {
    public UpdateStudyCommand toCommand(Long memberId, Long studyId) {
        return new UpdateStudyCommand(
                memberId,
                studyId,
                this.name,
                this.topic,
                this.startDate,
                this.endDate
        );
    }
}
