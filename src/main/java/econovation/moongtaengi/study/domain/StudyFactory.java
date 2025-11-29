package econovation.moongtaengi.study.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudyFactory {
    private final StudyRepository studyRepository;

    public Study createStudy(Long memberId, String rawName) {
        int hostedCount = studyRepository.countByMemberIdAndRole(memberId, StudyRole.HOST);

        if (hostedCount >= 5) {
            throw new IllegalArgumentException("스터디는 최대 5개까지만 생성 가능합니다.");
        }

        StudyName studyName = new StudyName(rawName);

        return new Study(studyName, memberId);
    }
}
