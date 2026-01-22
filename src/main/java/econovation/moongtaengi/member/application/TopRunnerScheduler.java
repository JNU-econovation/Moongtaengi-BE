package econovation.moongtaengi.member.application;

import econovation.moongtaengi.member.domain.event.TopRunnerSelectedEvent;
import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 탑러너 스케줄러
 * - 5분마다 경험치 상위 5명 선정
 * - TopRunnerSelectedEvent 발행 (컬렉션 해금은 CollectionEventListener가 처리)
 * - 한번 해금되면 순위에서 밀려나도 회수하지 않음
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TopRunnerScheduler {

    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 10분마다 실행 - 탑러너 선정
     * 경험치 상위 5명 선정 후 이벤트 발행
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void selectTopRunners() {
        log.info("탑러너 선정 스케줄 시작");

        try {
            // 경험치 상위 5명 조회
            List<Member> topMembers = memberRepository.findTopMembersByExperience(PageRequest.of(0, 5));

            if (topMembers.isEmpty()) {
                log.info("탑러너 선정 대상이 없습니다");
                return;
            }

            List<Long> memberIds = topMembers.stream()
                    .map(Member::getId)
                    .toList();

            log.info("탑러너 선정 완료 - 선정된 회원 수: {}, memberIds: {}", memberIds.size(), memberIds);

            // 탑러너 선정 이벤트 발행
            eventPublisher.publishEvent(new TopRunnerSelectedEvent(memberIds));

            log.info("탑러너 선정 이벤트 발행 완료");
        } catch (Exception e) {
            log.error("탑러너 선정 실패 - error: {}", e.getMessage(), e);
        }
    }
}
