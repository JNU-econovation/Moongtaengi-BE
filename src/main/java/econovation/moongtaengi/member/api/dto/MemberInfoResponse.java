package econovation.moongtaengi.member.api.dto;

import econovation.moongtaengi.member.domain.Member;

/**
 * 회원 본인 정보 조회 응답 DTO
 */
public record MemberInfoResponse(
        Long id,
        String nickname
) {
    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(
                member.getId(),
                member.getNickname().getValue()
        );
    }
}