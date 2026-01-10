package econovation.moongtaengi.study.domain.process;

import java.time.LocalDate;

public record StudyProcessPeriodBound(
        LocalDate minStartDate,
        LocalDate maxEndDate
) {
}
