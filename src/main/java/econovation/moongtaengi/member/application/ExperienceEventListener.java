package econovation.moongtaengi.member.application;

import econovation.moongtaengi.member.domain.QuestType;
import econovation.moongtaengi.member.domain.event.LoginSuccessEvent;
import econovation.moongtaengi.study.domain.event.StudyCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExperienceEventListener {
    private final ExperienceService experienceService;

    @EventListener
    public void handleLoginSuccess(LoginSuccessEvent event) {
        log.info("LoginSuccessEvent 수신 - memberId: {}", event.memberId());
        experienceService.completeQuest(event.memberId(), QuestType.LOGIN);
    }

    @EventListener
    public void handleStudyCreated(StudyCreatedEvent event) {
        log.info("StudyCreatedEvent 수신 - studyId: {}, memberId: {}", event.studyId(), event.memberId());
        experienceService.completeQuest(event.memberId(), QuestType.CREATE_STUDY);
    }
}
