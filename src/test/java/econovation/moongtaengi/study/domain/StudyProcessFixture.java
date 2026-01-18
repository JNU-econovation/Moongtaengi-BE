package econovation.moongtaengi.study.domain;

import econovation.moongtaengi.study.domain.process.StudyProcess;
import java.time.LocalDate;

public class StudyProcessFixture {

    public static final Integer DEFAULT_PROCESS_ORDER = 1;
    public static final String DEFAULT_TITLE = "테스트 프로세스";
    public static final LocalDate BASE_DATE = StudyFixture.FIXED_DATE;
    public static final String DEFAULT_ASSIGNMENT_DESCRIPTION = "테스트 과제";

    public static StudyProcess aStudyProcess(Long studyId) {
        return StudyProcess.create(
                studyId,
                DEFAULT_PROCESS_ORDER,
                DEFAULT_TITLE,
                BASE_DATE,
                BASE_DATE.plusDays(14),
                DEFAULT_ASSIGNMENT_DESCRIPTION
        );
    }
}
