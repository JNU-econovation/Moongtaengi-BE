package econovation.moongtaengi.home.api.dto;

/**
 * 탑러너 정보
 * @param nickname 닉네임
 * @param profileIconUrl 프로필 아이콘 URL
 */
public record TopRunnerInfo(
        String nickname,
        String profileIconUrl
) {
}
