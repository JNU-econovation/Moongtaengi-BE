package econovation.moongtaengi.study.domain;

import java.time.LocalDate;

public class StudyFixture {

    public static final LocalDate FIXED_DATE = LocalDate.of(2026, 1, 1);

    public static Study aStudy(Long hostId) {
        return new Study(
                new StudyName("테스트 스터디"),
                new StudyPeriod(FIXED_DATE.minusMonths(1), FIXED_DATE.plusMonths(1)),
                new StudyTopic("테스트 주제"),
                hostId,
                new InviteCode("12345678")
        );
    }
}
