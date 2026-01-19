package econovation.moongtaengi.member.domain.event;

/**
 * 회원 등록 완료 이벤트
 * 최종 회원가입이 완료될 때 발행
 */
public record MemberRegisteredEvent(
        Long memberId
) {
}