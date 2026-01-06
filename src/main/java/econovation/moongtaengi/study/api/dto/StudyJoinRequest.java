package econovation.moongtaengi.study.api.dto;

import jakarta.validation.constraints.NotBlank;

public record StudyJoinRequest(
        @NotBlank(message = "초대 코드는 필수입니다.")
        String inviteCode

) {
}
