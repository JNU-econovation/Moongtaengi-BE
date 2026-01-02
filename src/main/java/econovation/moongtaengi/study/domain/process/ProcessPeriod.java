package econovation.moongtaengi.study.domain.process;

import econovation.moongtaengi.study.domain.StudyErrorCode;
import econovation.moongtaengi.study.domain.StudyException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProcessPeriod {

    private static final int MIN_TOTAL_DAYS = 3;    // 최소 3일
    private static final int MAX_TOTAL_DAYS = 90;   // 최대 3개월

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    public ProcessPeriod(LocalDate startDate, LocalDate endDate) {
        validate(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getTotalDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    private void validate(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new StudyException(StudyErrorCode.PERIOD_NOT_NULL);
        }

        if (endDate.isBefore(startDate)) {
            throw new StudyException(StudyErrorCode.PERIOD_DATE_INVALID);
        }

        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        if (days < MIN_TOTAL_DAYS || days > MAX_TOTAL_DAYS) {
            throw new StudyException(
                    StudyErrorCode.PROCESS_PERIOD_DAYS_INVALID,
                    MIN_TOTAL_DAYS,
                    MAX_TOTAL_DAYS
            );
        }
    }
}
