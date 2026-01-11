package econovation.moongtaengi.study.domain.assignment;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class AssignmentDeadline {
    @Column(name = "deadline", nullable = false)
    private LocalDateTime value;

    private AssignmentDeadline(LocalDateTime value) {
        this.value = value;
    }

    public static AssignmentDeadline create(
            LocalDateTime requestDate,
            LocalDate startDate,
            LocalDate endDate) {
        LocalDateTime rangeStart = startDate.atStartOfDay();
        LocalDateTime rangeEnd = endDate.atTime(23, 59, 59);

        LocalDateTime targetDate = (requestDate != null) ? requestDate : rangeEnd;

        validate(targetDate, rangeStart, rangeEnd);

        return new AssignmentDeadline(targetDate);
    }

    private static void validate(LocalDateTime targetDate, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (targetDate.isBefore(rangeStart) || targetDate.isAfter(rangeEnd)) {
            throw new AssignmentException(AssignmentErrorCode.INVALID_ASSIGNMENT_INFO);
        }
    }
}
