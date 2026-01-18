package econovation.moongtaengi.notification.application;

import econovation.moongtaengi.gamification.application.DailyQuestService;
import econovation.moongtaengi.notification.domain.NotificationType;
import econovation.moongtaengi.study.domain.assignment.Assignment;
import econovation.moongtaengi.study.domain.assignment.AssignmentRepository;
import econovation.moongtaengi.study.domain.assignment.AssignmentStatus;
import econovation.moongtaengi.study.domain.process.StudyProcess;
import econovation.moongtaengi.study.domain.process.StudyProcessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 알림 스케줄러
 * - 일일 퀘스트 초기화 알림 (매일 자정)
 * - 마감 임박 알림 (매일 오전 9시)
 * - 미제출 알림 (마감 후)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationService notificationService;
    private final StudyProcessRepository studyProcessRepository;
    private final AssignmentRepository assignmentRepository;
    private final DailyQuestService dailyQuestService;

    /**
     * 매일 자정 - 일일 퀘스트 초기화
     * DailyQuestService를 통해 모든 일일 퀘스트 리셋 및 DB 반영
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void resetDailyQuests() {
        log.info("일일 퀘스트 초기화 스케줄 시작");
        try {
            dailyQuestService.resetAllDailyQuests();
        } catch (Exception e) {
            log.error("일일 퀘스트 초기화 실패 - error: {}", e.getMessage());
        }
    }

    /**
     * 매일 오전 9시 - 마감 임박 알림
     * 내일 마감인 프로세스의 미제출 과제에 대해 알림 전송
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void sendDeadlineSoonNotification() {
        log.info("마감 임박 알림 스케줄 시작");

        try {
            LocalDate tomorrow = LocalDate.now().plusDays(1);

            // DB에서 내일 마감인 프로세스만 조회 (최적화)
            List<StudyProcess> dueSoonProcesses = studyProcessRepository.findByEndDate(tomorrow);

            log.info("내일 마감 프로세스 수: {}", dueSoonProcesses.size());

            // 각 프로세스별로 미제출 과제 확인 및 알림 전송
            for (StudyProcess process : dueSoonProcesses) {
                // DB에서 해당 프로세스의 WAITING 상태 과제만 조회 (최적화)
                List<Assignment> waitingAssignments = assignmentRepository.findByProcessIdAndStatus(
                        process.getId(), AssignmentStatus.WAITING);

                for (Assignment assignment : waitingAssignments) {
                    String message = String.format("'%s' 과제 마감이 내일입니다!", process.getTitle());
                    notificationService.createNotification(
                            assignment.getAssigneeId(),
                            NotificationType.ASSIGNMENT_DEADLINE_SOON,
                            message,
                            "ASSIGNMENT"
                    );
                }

                log.info("프로세스 '{}' 마감 임박 알림 전송 완료 - 대상: {}명",
                        process.getTitle(), waitingAssignments.size());
            }
        } catch (Exception e) {
            log.error("마감 임박 알림 전송 실패 - error: {}", e.getMessage());
        }
    }

    /**
     * 매일 자정 - 미제출 알림
     * 마감일로부터 1일, 7일 경과 시점에 알림 전송
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void sendOverdueNotification() {
        log.info("미제출 알림 스케줄 시작");

        try {
            LocalDate oneDayAgo = LocalDate.now().minusDays(1);
            LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);

            // DB에서 1일 또는 7일 전에 마감된 프로세스만 조회 (최적화)
            List<StudyProcess> overdueProcesses = studyProcessRepository.findByEndDateIn(
                    List.of(oneDayAgo, sevenDaysAgo));

            log.info("미제출 알림 대상 프로세스 수: {}", overdueProcesses.size());

            // 각 프로세스별로 미제출 과제 확인 및 알림 전송
            for (StudyProcess process : overdueProcesses) {
                // DB에서 해당 프로세스의 WAITING 상태 과제만 조회 (최적화)
                List<Assignment> overdueAssignments = assignmentRepository.findByProcessIdAndStatus(
                        process.getId(), AssignmentStatus.WAITING);

                for (Assignment assignment : overdueAssignments) {
                    long daysOverdue = LocalDate.now().toEpochDay() - process.getEndDate().toEpochDay();
                    String message = String.format("'%s' 과제를 아직 제출하지 않았습니다. (마감 후 %d일)",
                            process.getTitle(), daysOverdue);

                    notificationService.createNotification(
                            assignment.getAssigneeId(),
                            NotificationType.ASSIGNMENT_OVERDUE,
                            message,
                            "ASSIGNMENT"
                    );
                }

                log.info("프로세스 '{}' 미제출 알림 전송 완료 - 대상: {}명",
                        process.getTitle(), overdueAssignments.size());
            }
        } catch (Exception e) {
            log.error("미제출 알림 전송 실패 - error: {}", e.getMessage());
        }
    }
}
