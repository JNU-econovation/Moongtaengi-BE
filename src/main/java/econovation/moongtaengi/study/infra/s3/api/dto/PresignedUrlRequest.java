package econovation.moongtaengi.study.infra.s3.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PresignedUrlRequest(
        @NotNull(message = "스터디 ID는 필수입니다")
        Long studyId,

        @NotNull(message = "프로세스 ID는 필수입니다")
        Long processId,

        @NotBlank(message = "파일 이름은 필수입니다")
        String fileName,

        @NotNull(message = "파일 크기는 필수입니다")
        @Min(value = 1, message = "파일 크기는 1바이트 이상이어야 합니다")
        Long fileSize,

        @NotBlank(message = "Content-Type은 필수입니다")
        String contentType
) {
}
