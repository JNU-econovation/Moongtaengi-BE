package econovation.moongtaengi.study.domain.assignment;


import java.time.LocalDate;

public interface ProcessInfoProvider {

    ProcessInfo getProcessInfo(Long processId);

    record ProcessInfo(Long studyId, LocalDate startDate, LocalDate endDate) {}
}
