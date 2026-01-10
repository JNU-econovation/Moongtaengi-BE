package econovation.moongtaengi.admin.api.dto;

import econovation.moongtaengi.study.domain.Study;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AdminStudyResponse(
        Long id,
        String name,
        String topic,
        LocalDate startDate,
        LocalDate endDate,
        int memberCount,
        String inviteCode,
        LocalDateTime createdAt
) {
    public static AdminStudyResponse from(Study study) {
        return new AdminStudyResponse(
                study.getId(),
                study.getName().getValue(),
                study.getTopic().getValue(),
                study.getPeriod().getStartDate(),
                study.getPeriod().getEndDate(),
                study.getMembers().size(),
                study.getInviteCode().getValue(),
                study.getCreatedAt()
        );
    }
}
