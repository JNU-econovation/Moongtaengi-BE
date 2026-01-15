package econovation.moongtaengi.study.infra;

import econovation.moongtaengi.study.domain.assignment.AssignmentErrorCode;
import econovation.moongtaengi.study.domain.assignment.AssignmentException;
import econovation.moongtaengi.study.domain.assignment.ProcessInfoProvider;
import econovation.moongtaengi.study.domain.process.StudyProcess;
import econovation.moongtaengi.study.domain.process.StudyProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProcessInfoProviderImpl implements ProcessInfoProvider {

    private final StudyProcessRepository studyProcessRepository;

    @Override
    public ProcessInfo getProcessInfo(Long processId) {

        StudyProcess studyProcess = studyProcessRepository.findById(processId).
                orElseThrow(() -> new AssignmentException(AssignmentErrorCode.INVALID_ASSIGNMENT_INFO));


        return new ProcessInfo(
                studyProcess.getStudyId(),
                studyProcess.getStartDate(),
                studyProcess.getEndDate());
    }
}
