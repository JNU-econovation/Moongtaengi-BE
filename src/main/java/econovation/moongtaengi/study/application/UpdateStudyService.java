package econovation.moongtaengi.study.application;

import econovation.moongtaengi.study.domain.Study;
import econovation.moongtaengi.study.domain.StudyErrorCode;
import econovation.moongtaengi.study.domain.StudyException;
import econovation.moongtaengi.study.domain.StudyName;
import econovation.moongtaengi.study.domain.StudyPeriod;
import econovation.moongtaengi.study.domain.StudyPeriodValidator;
import econovation.moongtaengi.study.domain.StudyRepository;
import econovation.moongtaengi.study.domain.StudyTopic;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateStudyService {
    private final StudyRepository studyRepository;
    private final StudyPeriodValidator studyPeriodValidator;

    @Transactional
    public void updateStudy(UpdateStudyCommand command) {
        Study study = studyRepository.findByIdWithMembers(command.studyId())
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        StudyName name = command.name() != null ?
                new StudyName(command.name()) : study.getName();

        StudyTopic topic = command.topic() != null ?
                new StudyTopic(command.topic()) : study.getTopic();

        LocalDate startDate = command.startDate() != null ?
                command.startDate() : study.getPeriod().getStartDate();

        LocalDate endDate = command.endDate() != null ?
                command.endDate() : study.getPeriod().getEndDate();

        StudyPeriod period = new StudyPeriod(startDate, endDate);

        studyPeriodValidator.validate(study.getId(), period);

        study.update(command.memberId(), name, period, topic);
    }
}
