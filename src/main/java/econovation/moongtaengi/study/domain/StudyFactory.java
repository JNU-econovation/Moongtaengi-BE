package econovation.moongtaengi.study.domain;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudyFactory {
    private final StudyRepository studyRepository;

    private static final int MAX_HOSTED_COUNT = 5;

    public Study createStudy(Long memberId,
            String rawName,
            LocalDate startDate,
            LocalDate endDate,
            String topic) {
        int hostedCount = studyRepository.countByMemberIdAndRole(memberId, StudyRole.HOST);

        if (hostedCount >= MAX_HOSTED_COUNT) {
            throw new StudyCreateLimitException(MAX_HOSTED_COUNT);
        }

        StudyName studyName = new StudyName(rawName);
        StudyPeriod studyPeriod = new StudyPeriod(startDate, endDate);
        StudyTopic studyTopic = new StudyTopic(topic);

        return new Study(studyName, studyPeriod, studyTopic, memberId);
    }
}
