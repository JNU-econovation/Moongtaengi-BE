package econovation.moongtaengi.study.domain.process;

import econovation.moongtaengi.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "study_processes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyProcess extends BaseEntity {

    @Column(nullable = false)
    private Long studyId;

    @Column(nullable = false)
    private Integer processOrder; // 프로세스 순서

    @Column(nullable = false, length = 100)
    private String title;  // 프로세스 제목

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, length = 100)
    private String topic;  // 학습 주제 (1개)

    @Column(nullable = false)
    private String assignmentDescription;  // 과제 소개

    public StudyProcess(
            Long studyId,
            Integer processOrder,
            String title,
            LocalDate startDate,
            LocalDate endDate,
            String topic,
            String assignmentDescription
    ) {
        validate();
        this.studyId = studyId;
        this.processOrder = processOrder;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.topic = topic;
        this.assignmentDescription = assignmentDescription;
    }

    private void validate() {
        if (studyId == null) {
            throw new IllegalArgumentException("스터디 ID는 필수입니다.");
        }
        if (processOrder == null || processOrder < 1) {
            throw new IllegalArgumentException("프로세스 순서는 1 이상이어야 합니다.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("프로세스 제목은 필수입니다.");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("시작일과 종료일은 필수입니다.");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("종료일이 시작일보다 앞설 수 없습니다.");
        }
        if (topic == null || topic.isBlank()) {
            throw new IllegalArgumentException("학습 주제는 필수입니다.");
        }
        if (assignmentDescription == null || assignmentDescription.isBlank()) {
            throw new IllegalArgumentException("과제 설명은 필수입니다.");
        }
    }

    public long getDurationDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

}
