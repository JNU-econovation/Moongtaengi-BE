package econovation.moongtaengi.study.api.dto;

import econovation.moongtaengi.study.domain.process.StudyProcess;

import java.time.LocalDate;

public record ProcessResponse(
        Long id,
        Integer processOrder,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        Long durationDays,
        String memo,
        String assignmentDescription
) {
    public static ProcessResponse from(StudyProcess process) {
        return new ProcessResponse(
                process.getId(),
                process.getProcessOrder(),
                process.getTitle(),
                process.getStartDate(),
                process.getEndDate(),
                process.getDurationDays(),
                process.getMemo().getValue(),
                process.getAssignmentDescription()
        );
    }
}
