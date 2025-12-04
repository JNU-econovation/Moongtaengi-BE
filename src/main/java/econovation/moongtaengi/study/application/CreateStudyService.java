package econovation.moongtaengi.study.application;

import econovation.moongtaengi.study.domain.Study;
import econovation.moongtaengi.study.domain.StudyFactory;
import econovation.moongtaengi.study.domain.StudyRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateStudyService {
    private final StudyFactory studyFactory;
    private final StudyRepository studyRepository;

    @Transactional
    public Long createStudy(Long memberId,
            String name,
            String topic,
            LocalDate startDate,
            LocalDate endDate) {
        Study study = studyFactory.createStudy(
                memberId,
                name,
                startDate,
                endDate,
                topic
        );

        studyRepository.save(study);

        return study.getId();
    }
}
