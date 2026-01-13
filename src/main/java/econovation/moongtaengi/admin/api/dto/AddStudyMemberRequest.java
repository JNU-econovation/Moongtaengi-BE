package econovation.moongtaengi.admin.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record AddStudyMemberRequest(
        @NotNull(message = "스터디 ID는 필수입니다")
        Long studyId,

        @NotEmpty(message = "추가할 멤버 ID 목록은 필수입니다")
        List<Long> memberIds
) {}
