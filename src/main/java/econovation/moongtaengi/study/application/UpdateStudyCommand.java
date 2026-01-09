package econovation.moongtaengi.study.application;

import java.time.LocalDate;

public record UpdateStudyCommand(
        Long memberId,
        Long studyId,
        String name,
        String topic,
        LocalDate startDate,
        LocalDate endDate
) {
}
