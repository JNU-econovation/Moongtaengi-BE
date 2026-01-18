package econovation.moongtaengi.member.domain.event;

/**
 * 경험치 획득 이벤트
 * 회원이 경험치를 획득할 때 발행되어 경험치 기반 컬렉션 해금 조건 체크에 사용
 */
public record ExperienceAddedEvent(
        Long memberId,
        int addedExperience,
        int totalExperience
) {
}