package econovation.moongtaengi.admin.api.dto;

import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.MemberStatus;
import econovation.moongtaengi.member.domain.Role;
import java.time.LocalDateTime;

public record AdminMemberResponse(
        Long id,
        String kakaoId,
        String nickname,
        MemberStatus status,
        Role role,
        LocalDateTime createdAt
) {
    public static AdminMemberResponse from(Member member) {
        return new AdminMemberResponse(
                member.getId(),
                member.getKakaoId(),
                member.getNickname() != null ? member.getNickname().getValue() : null,
                member.getStatus(),
                member.getRole(),
                member.getCreatedAt()
        );
    }
}
