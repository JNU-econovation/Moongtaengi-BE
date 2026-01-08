package econovation.moongtaengi.study.domain;

import econovation.moongtaengi.study.domain.process.StudyProcessPeriodBound;
import econovation.moongtaengi.study.domain.process.StudyProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudyPeriodValidator {
    private final StudyProcessRepository studyProcessRepository;

    public void validate(Long studyId, StudyPeriod newPeriod) {
        StudyProcessPeriodBound bound = studyProcessRepository.findProcessPeriodBound(studyId);

        if (bound.minStartDate() == null || bound.maxEndDate() == null) {
            return;
        }

        if (newPeriod.getEndDate().isBefore(bound.maxEndDate())) {
            throw new StudyException(StudyErrorCode.STUDY_END_DATE_TOO_EARLY);
        }

        if (newPeriod.getStartDate().isAfter(bound.minStartDate())) {
            throw new StudyException(StudyErrorCode.STUDY_START_DATE_TOO_LATE);
        }
    }
}
