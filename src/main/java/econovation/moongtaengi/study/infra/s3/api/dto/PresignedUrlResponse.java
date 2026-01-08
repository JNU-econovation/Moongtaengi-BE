package econovation.moongtaengi.study.infra.s3.api.dto;

/**
 * Presigned URL 발급 응답 DTO
 */
public record PresignedUrlResponse(
        String uploadUrl,      // 업로드용 Presigned URL
        String fileUrl,        // 파일 접근 URL (마크다운에 삽입)
        String fileName,       // 저장된 파일 이름 (UUID 포함)
        int expiresIn          // 만료 시간 (초)
) {
}
