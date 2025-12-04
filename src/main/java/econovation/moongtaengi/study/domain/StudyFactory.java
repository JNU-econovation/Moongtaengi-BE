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

        InviteCode inviteCode = generateUniqueInviteCode();

        return new Study(studyName, studyPeriod, studyTopic, memberId, inviteCode);
    }

    private InviteCode generateUniqueInviteCode() {
        InviteCode inviteCode;
        int retryCount = 0;

        do {
            inviteCode = InviteCode.generate();
            retryCount++;

            if (retryCount > 5) {
                throw new IllegalStateException("초대 코드 생성에 실패했습니다.");
            }
        } while (studyRepository.existsByInviteCodeValue(inviteCode.getValue()));

        return inviteCode;
    }
}
