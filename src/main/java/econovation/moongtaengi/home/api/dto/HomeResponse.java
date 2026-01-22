package econovation.moongtaengi.home.api.dto;

/**
 * 메인 페이지 응답
 * @param nickname 사용자 닉네임
 * @param equippedCollectionName 장착한 컬렉션 이름
 * @param equippedCollectionIconUrl 장착한 컬렉션 아이콘 URL
 * @param equippedCollectionBackgroundUrl 장착한 컬렉션 배경 URL
 * @param cheeringMessage 응원 메시지
 * @param commentCount 댓글 수
 * @param activityTitle 활동 칭호
 * @param collectionCount 보유 컬렉션 수
 * @param totalExperience 총 경험치
 * @param shortcutStudyId 바로가기 스터디 ID (없으면 null)
 */
public record HomeResponse(
        String nickname,
        String equippedCollectionName,
        String equippedCollectionIconUrl,
        String equippedCollectionBackgroundUrl,
        String cheeringMessage,
        Long commentCount,
        String activityTitle,
        Long collectionCount,
        int totalExperience,
        Long shortcutStudyId
) {
}
