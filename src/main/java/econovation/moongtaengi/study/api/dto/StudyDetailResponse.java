package econovation.moongtaengi.study.api.dto;

import econovation.moongtaengi.study.domain.Study;
import econovation.moongtaengi.study.domain.StudyMember;
import econovation.moongtaengi.study.domain.StudyPeriod;
import econovation.moongtaengi.study.domain.StudyRole;
import java.time.LocalDate;

public record StudyDetailResponse(
        Long id,
        String name,
        StudyPeriodDto period,
        String topic,
        String inviteCode,
        StudyRole myRole
) {
    public record StudyPeriodDto(
            LocalDate startDate,
            LocalDate endDate
    ) {
        public static StudyPeriodDto from(StudyPeriod studyPeriod) {
            return new StudyPeriodDto(studyPeriod.getStartDate(), studyPeriod.getEndDate());
        }
    }

    public static StudyDetailResponse of(Study study, StudyMember studyMember) {
        return new StudyDetailResponse(
                study.getId(),
                study.getName().getValue(),
                StudyPeriodDto.from(study.getPeriod()),
                study.getTopic().getValue(),
                study.getInviteCode().getValue(),
                studyMember.getRole()
        );
    }
}
