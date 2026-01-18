package econovation.moongtaengi.notification.application;

import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.MemberRepository;
import econovation.moongtaengi.notification.domain.NotificationType;
import econovation.moongtaengi.study.domain.StudyMember;
import econovation.moongtaengi.study.domain.StudyMemberRepository;
import econovation.moongtaengi.study.domain.assignment.Assignment;
import econovation.moongtaengi.study.domain.assignment.AssignmentRepository;
import econovation.moongtaengi.study.domain.assignment.ProcessInfoProvider;
import econovation.moongtaengi.study.domain.submission.SubmissionCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

/**
 * 알림 이벤트 리스너
 * - 메인 트랜잭션 커밋 후 새 트랜잭션에서 알림 처리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;
    private final AssignmentRepository assignmentRepository;
    private final ProcessInfoProvider processInfoProvider;
    private final StudyMemberRepository studyMemberRepository;
    private final MemberRepository memberRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleSubmissionCreated(SubmissionCreatedEvent event) {
        log.info("SubmissionCreatedEvent 수신 - assignmentId: {}, submitterId: {}",
                event.assignmentId(), event.submitterId());

        try {
            // 1. Assignment 조회 → processId 획득
            Assignment assignment = assignmentRepository.findById(event.assignmentId())
                    .orElseThrow(() -> new IllegalArgumentException("과제를 찾을 수 없습니다."));

            // 2. ProcessInfo 조회 → studyId 획득
            ProcessInfoProvider.ProcessInfo processInfo = processInfoProvider.getProcessInfo(assignment.getProcessId());

            // 3. 스터디 멤버 전체 조회
            List<StudyMember> studyMembers = studyMemberRepository.findAllByStudyId(processInfo.studyId());

            // 4. 제출자 정보 조회 (닉네임)
            Member submitter = memberRepository.findById(event.submitterId())
                    .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
            String submitterNickname = submitter.getNickname().getValue();

            // 5. 제출자를 제외한 모든 스터디원에게 알림 생성
            studyMembers.stream()
                    .filter(member -> !member.getMemberId().equals(event.submitterId()))
                    .forEach(member -> {
                        String message = String.format("%s님이 과제를 제출했습니다.", submitterNickname);
                        notificationService.createNotification(
                                member.getMemberId(),
                                NotificationType.ASSIGNMENT_SUBMITTED,
                                message,
                                "SUBMISSION"
                        );
                    });

            log.info("과제 제출 알림 생성 완료 - 대상: {}명", studyMembers.size() - 1);
        } catch (Exception e) {
            log.error("과제 제출 알림 생성 실패 - assignmentId: {}, error: {}",
                    event.assignmentId(), e.getMessage());
        }
    }
}
