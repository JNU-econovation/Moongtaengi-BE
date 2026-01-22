package econovation.moongtaengi.member.domain.event;

import java.util.List;

/**
 * 탑러너 선정 이벤트
 * 스케줄러가 경험치 상위 회원을 선정한 후 발행
 *
 * @param memberIds 선정된 탑러너 회원 ID 목록
 */
public record TopRunnerSelectedEvent(
        List<Long> memberIds
) {
}
