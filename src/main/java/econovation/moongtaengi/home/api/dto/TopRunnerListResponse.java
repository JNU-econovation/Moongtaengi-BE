package econovation.moongtaengi.home.api.dto;

import java.util.List;

/**
 * 탑러너 목록 응답
 * @param topRunners 탑러너 목록 (최대 5명)
 */
public record TopRunnerListResponse(
        List<TopRunnerInfo> topRunners
) {
}