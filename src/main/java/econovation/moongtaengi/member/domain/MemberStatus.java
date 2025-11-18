package econovation.moongtaengi.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus {
    TEMPORARY("임시회원"),    // 카카오 로그인만 완료, 추가정보 입력 필요
    ACTIVE("활성회원");

    private final String description;
}
