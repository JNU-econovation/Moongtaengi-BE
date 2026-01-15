package econovation.moongtaengi.study.infra;

import econovation.moongtaengi.study.domain.assignment.AssignmentErrorCode;
import econovation.moongtaengi.study.domain.assignment.AssignmentException;
import econovation.moongtaengi.study.domain.assignment.ProcessDateRangeProvider;
import econovation.moongtaengi.study.domain.process.StudyProcess;
import econovation.moongtaengi.study.domain.process.StudyProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProcessDateRangeProviderImpl implements ProcessDateRangeProvider {

    private final StudyProcessRepository studyProcessRepository;

    @Override
    public DateRange getDateRange(Long processId) {

        StudyProcess studyProcess = studyProcessRepository.findById(processId).
                orElseThrow(() -> new AssignmentException(AssignmentErrorCode.INVALID_ASSIGNMENT_INFO));


        return new DateRange(
                studyProcess.getStartDate(),
                studyProcess.getEndDate()
        );
    }
}
