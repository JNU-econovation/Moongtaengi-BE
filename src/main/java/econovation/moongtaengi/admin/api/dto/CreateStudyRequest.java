package econovation.moongtaengi.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateStudyRequest(
        @NotBlank(message = "스터디 이름은 필수입니다")
        String name,

        @NotBlank(message = "스터디 주제는 필수입니다")
        String topic,

        @NotNull(message = "시작일은 필수입니다")
        LocalDate startDate,

        @NotNull(message = "종료일은 필수입니다")
        LocalDate endDate,

        @NotNull(message = "호스트 ID는 필수입니다")
        Long hostId
) {}
