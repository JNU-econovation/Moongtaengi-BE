package econovation.moongtaengi.study.infra.s3.api;

import econovation.moongtaengi.global.annotation.LoginMemberId;
import econovation.moongtaengi.study.infra.s3.api.dto.PresignedUrlRequest;
import econovation.moongtaengi.study.infra.s3.api.dto.PresignedUrlResponse;
import econovation.moongtaengi.study.infra.s3.application.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    /**
     * Presigned URL 발급
     * POST /api/s3/presigned-url
     */
    @PostMapping("/presigned-url")
    public ResponseEntity<PresignedUrlResponse> generatePresignedUrl(
            @LoginMemberId Long memberId,
            @Valid @RequestBody PresignedUrlRequest request
    ) {
        log.info("Presigned URL 발급 API 호출 - memberId: {}, studyId: {}, fileName: {}",
                memberId, request.studyId(), request.fileName());

        PresignedUrlResponse response = s3Service.generatePresignedUrl(request, memberId);

        log.info("✅ Presigned URL 발급 완료 - fileName: {}", response.fileName());

        return ResponseEntity.ok(response);
    }
}
