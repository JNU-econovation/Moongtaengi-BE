package econovation.moongtaengi.member.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompleteRegistrationRequest(
    @NotBlank(message = "닉네임은 필수입니다")
    @Size(min = 2, max = 7, message = "닉네임은 2자 이상 7자 이하여야 합니다")
    String nickname
) {
}
