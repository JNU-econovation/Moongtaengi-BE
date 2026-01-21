package econovation.moongtaengi.member.application;

import econovation.moongtaengi.gamification.domain.QuestType;
import econovation.moongtaengi.member.domain.event.LoginSuccessEvent;
import econovation.moongtaengi.study.domain.comment.CommentCreatedEvent;
import econovation.moongtaengi.study.domain.event.StudyCreatedEvent;
import econovation.moongtaengi.study.domain.submission.SubmissionCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExperienceEventListener {
    private final ExperienceService experienceService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleLoginSuccess(LoginSuccessEvent event) {
        log.info("LoginSuccessEvent 수신 - memberId: {}", event.memberId());
        experienceService.completeQuest(event.memberId(), QuestType.LOGIN);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleStudyCreated(StudyCreatedEvent event) {
        log.info("StudyCreatedEvent 수신 - studyId: {}, memberId: {}", event.studyId(), event.memberId());
        experienceService.completeQuest(event.memberId(), QuestType.CREATE_STUDY);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleSubmissionCreated(SubmissionCreatedEvent event) {
        log.info("SubmissionCreatedEvent 수신 - assignmentId: {}, submitterId: {}", event.assignmentId(), event.submitterId());
        experienceService.completeQuest(event.submitterId(), QuestType.SUBMIT_ASSIGNMENT);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCommentCreated(CommentCreatedEvent event) {
        log.info("CommentCreatedEvent 수신 - commentId: {}, commenterId: {}", event.commentId(), event.commenterId());
        experienceService.completeQuest(event.commenterId(), QuestType.COMMENT);
    }
}
