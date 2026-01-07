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
        String assignmentDescription,
        ProcessStatus status
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
                process.getAssignmentDescription(),
                calculateStatus(process.getStartDate(), process.getEndDate())
        );
    }

    private static ProcessStatus calculateStatus(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();

        if (today.isBefore(startDate)) {
            return ProcessStatus.NOT_STARTED;  // 예정
        } else if (today.isAfter(endDate)) {
            return ProcessStatus.COMPLETED;    // 완료
        } else {
            return ProcessStatus.IN_PROGRESS;  // 진행 중
        }
    }

}
