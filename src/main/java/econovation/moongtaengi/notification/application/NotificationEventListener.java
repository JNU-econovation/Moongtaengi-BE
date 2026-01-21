package econovation.moongtaengi.notification.application;

import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.MemberRepository;
import econovation.moongtaengi.notification.domain.NotificationType;
import econovation.moongtaengi.study.domain.StudyMember;
import econovation.moongtaengi.study.domain.StudyMemberRepository;
import econovation.moongtaengi.study.domain.assignment.Assignment;
import econovation.moongtaengi.study.domain.assignment.AssignmentRepository;
import econovation.moongtaengi.study.domain.assignment.ProcessInfoProvider;
import econovation.moongtaengi.study.domain.comment.CommentCreatedEvent;
import econovation.moongtaengi.study.domain.submission.Submission;
import econovation.moongtaengi.study.domain.submission.SubmissionCreatedEvent;
import econovation.moongtaengi.study.domain.submission.SubmissionRepository;
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
    private final SubmissionRepository submissionRepository;

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

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCommentCreated(CommentCreatedEvent event) {
        log.info("CommentCreatedEvent 수신 - commentId: {}, submissionId: {}, commenterId: {}",
                event.commentId(), event.submissionId(), event.commenterId());

        try {
            // 1. Submission 조회 → submitterId 획득
            Submission submission = submissionRepository.findById(event.submissionId())
                    .orElseThrow(() -> new IllegalArgumentException("제출물을 찾을 수 없습니다."));

            // 2. 댓글 작성자가 제출물 소유자인 경우 알림 생성하지 않음
            if (submission.getSubmitterId().equals(event.commenterId())) {
                log.info("댓글 작성자가 제출물 소유자와 동일하여 알림을 생성하지 않습니다.");
                return;
            }

            // 3. 댓글 작성자 정보 조회 (닉네임)
            Member commenter = memberRepository.findById(event.commenterId())
                    .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
            String commenterNickname = commenter.getNickname().getValue();

            // 4. 제출물 소유자에게 알림 생성
            String message = String.format("%s님이 회원님의 과제에 댓글을 남겼습니다.", commenterNickname);
            notificationService.createNotification(
                    submission.getSubmitterId(),
                    NotificationType.COMMENT_RECEIVED,
                    message,
                    "COMMENT"
            );

            log.info("댓글 알림 생성 완료 - submitterId: {}, commenterId: {}",
                    submission.getSubmitterId(), event.commenterId());
        } catch (Exception e) {
            log.error("댓글 알림 생성 실패 - commentId: {}, error: {}",
                    event.commentId(), e.getMessage());
        }
    }
}
