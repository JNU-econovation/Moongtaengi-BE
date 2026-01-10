package econovation.moongtaengi.study.infra.s3.application;

import econovation.moongtaengi.study.infra.s3.api.dto.PresignedUrlRequest;
import econovation.moongtaengi.study.infra.s3.api.dto.PresignedUrlResponse;
import econovation.moongtaengi.study.infra.s3.config.S3Properties;
import econovation.moongtaengi.study.infra.s3.exception.S3ErrorCode;
import econovation.moongtaengi.study.infra.s3.exception.S3Exception;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

/**
 * S3 관련 비즈니스 로직
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final S3Properties s3Properties;

    /**
     * Presigned URL 발급
     */
    public PresignedUrlResponse generatePresignedUrl(
            PresignedUrlRequest request,
            Long memberId
    ) {
        // 1. 파일 검증
        validateFile(request.fileName(), request.fileSize(), request.contentType());

        // 2. S3 키 생성
        String s3Key = buildS3Key(
                request.studyId(),
                request.processId(),
                memberId,
                request.fileName()
        );

        log.info("Presigned URL 생성 시작 - memberId: {}, s3Key: {}", memberId, s3Key);

        // 3. Presigned URL 생성
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(s3Key)
                    .contentType(request.contentType())
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofSeconds(s3Properties.getPresignedUrlExpiration()))
                    .putObjectRequest(putObjectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

            String uploadUrl = presignedRequest.url().toString();
            String fileUrl = generateFileUrl(s3Key);
            String fileName = extractFileName(s3Key);

            log.info("✅ Presigned URL 생성 완료 - fileName: {}", fileName);

            return new PresignedUrlResponse(
                    uploadUrl,
                    fileUrl,
                    fileName,
                    s3Properties.getPresignedUrlExpiration()
            );

        } catch (Exception e) {
            log.error("Presigned URL 생성 실패 - s3Key: {}, error: {}", s3Key, e.getMessage());
            throw new S3Exception(S3ErrorCode.PRESIGNED_URL_GENERATION_FAILED);
        }
    }

    /**
     * 파일 검증
     */
    private void validateFile(String fileName, Long fileSize, String contentType) {
        // 1. Content-Type 검증
        if (!s3Properties.getAllowedContentTypes().contains(contentType)) {
            log.warn("지원하지 않는 파일 형식 - contentType: {}", contentType);
            throw new S3Exception(S3ErrorCode.INVALID_FILE_TYPE);
        }

        // 2. 파일 크기 검증
        if (fileSize > s3Properties.getMaxFileSize()) {
            long maxSizeMB = s3Properties.getMaxFileSize() / (1024 * 1024);
            log.warn("파일 크기 초과 - fileSize: {}, max: {}MB", fileSize, maxSizeMB);
            throw new S3Exception(S3ErrorCode.FILE_SIZE_EXCEEDED, maxSizeMB);
        }

        // 3. 파일 이름 검증
        if (fileName == null || fileName.isBlank()) {
            throw new S3Exception(S3ErrorCode.INVALID_FILE_NAME);
        }

        log.debug("파일 검증 완료 - fileName: {}, size: {}, type: {}",
                fileName, fileSize, contentType);
    }

    /**
     * S3 키 생성
     *
     * 경로: assignments/{studyId}/{processId}/{memberId}/{uuid}_{fileName}
     */
    private String buildS3Key(Long studyId, Long processId, Long memberId, String fileName) {
        String uuid = UUID.randomUUID().toString();
        String sanitizedFileName = sanitizeFileName(fileName);

        return String.format("assignments/%d/%d/%d/%s_%s",
                studyId, processId, memberId, uuid, sanitizedFileName);
    }

    /**
     * 파일 이름 정제 (특수문자 제거)
     */
    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9._-가-힣]", "_");
    }

    /**
     * 파일 URL 생성
     */
    private String generateFileUrl(String s3Key) {
        try {
            return s3Client.utilities()
                    .getUrl(builder -> builder
                            .bucket(s3Properties.getBucketName())
                            .key(s3Key))
                    .toString();
        } catch (Exception e) {
            log.error("파일 URL 생성 실패 - bucket: {}, key: {}, error: {}",
                    s3Properties.getBucketName(), s3Key, e.getMessage());
            throw new S3Exception(S3ErrorCode.PRESIGNED_URL_GENERATION_FAILED);
        }
    }

    /**
     * S3 키에서 파일 이름 추출
     */
    private String extractFileName(String s3Key) {
        return s3Key.substring(s3Key.lastIndexOf('/') + 1);
    }
}
