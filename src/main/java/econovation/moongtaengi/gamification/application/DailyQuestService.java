package econovation.moongtaengi.gamification.application;

import econovation.moongtaengi.gamification.domain.DailyQuest;
import econovation.moongtaengi.gamification.domain.DailyQuestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 일일 퀘스트 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DailyQuestService {

    private final DailyQuestRepository dailyQuestRepository;

    /**
     * 모든 일일 퀘스트의 날짜 리셋 처리
     * 스케줄러에서 자정에 호출하여 날짜가 변경된 퀘스트를 초기화하고 DB에 반영
     */
    @Transactional
    public void resetAllDailyQuests() {
        log.info("일일 퀘스트 리셋 시작");

        try {
            List<DailyQuest> allQuests = dailyQuestRepository.findAll();

            // canComplete()를 호출하면 내부적으로 resetIfNewDay()가 실행됨
            // 변경된 엔티티는 더티 체킹으로 자동 저장됨
            allQuests.forEach(quest -> {
                boolean canCompleteBefore = quest.canComplete();
                // canComplete 호출 시 resetIfNewDay가 실행되어 필요시 리셋됨
            });

            log.info("일일 퀘스트 리셋 완료 - 대상: {}개", allQuests.size());
        } catch (Exception e) {
            log.error("일일 퀘스트 리셋 실패 - error: {}", e.getMessage(), e);
            throw e;
        }
    }
}