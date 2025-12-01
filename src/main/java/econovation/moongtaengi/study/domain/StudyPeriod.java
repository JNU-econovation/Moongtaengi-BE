package econovation.moongtaengi.study.domain;

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
public class StudyPeriod {
    private static final int MIN_TOTAL_DAYS = 1;
    private static final int MAX_TOTAL_DAYS = 730;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    public StudyPeriod(LocalDate startDate, LocalDate endDate) {
        validate(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getTotalDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    private void validate(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("시작일과 종료일은 필수입니다.");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("종료일이 시작일보다 앞에 있을 수 없습니다.");
        }

        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        if (days < MIN_TOTAL_DAYS || days > MAX_TOTAL_DAYS) {
            throw new IllegalArgumentException("기간은 " + MIN_TOTAL_DAYS + " 이상 " + MAX_TOTAL_DAYS + " 이하여야 합니다.");
        }
    }
}
