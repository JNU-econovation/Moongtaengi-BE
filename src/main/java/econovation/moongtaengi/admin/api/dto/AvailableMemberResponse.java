package econovation.moongtaengi.admin.api.dto;

import econovation.moongtaengi.member.domain.Member;

public record AvailableMemberResponse(
        Long id,
        String kakaoId,
        String nickname,
        boolean isInStudy  // 이미 스터디에 참여 중인지
) {
    public static AvailableMemberResponse of(Member member, boolean isInStudy) {
        return new AvailableMemberResponse(
                member.getId(),
                member.getKakaoId(),
                member.getNickname() != null ? member.getNickname().getValue() : null,
                isInStudy
        );
    }
}
