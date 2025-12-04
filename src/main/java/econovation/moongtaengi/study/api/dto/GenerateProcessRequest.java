package econovation.moongtaengi.study.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GenerateProcessRequest(
        @NotBlank(message = "추가 설명은 필수입니다")
        @Size(max = 500, message = "추가 설명은 최대 500자입니다")
        String additionalDescription
) {
}
