package econovation.moongtaengi.admin.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateMemberRequest(
        @NotBlank(message = "카카오 ID는 필수입니다")
        String kakaoId,

        @NotBlank(message = "닉네임은 필수입니다")
        String nickname
) {}
