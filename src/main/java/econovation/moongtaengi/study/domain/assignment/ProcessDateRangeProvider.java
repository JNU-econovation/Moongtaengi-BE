package econovation.moongtaengi.study.domain.assignment;


import java.time.LocalDate;

public interface ProcessDateRangeProvider {

    DateRange getDateRange(Long processId);

    record DateRange(LocalDate startDate, LocalDate endDate) {}
}
