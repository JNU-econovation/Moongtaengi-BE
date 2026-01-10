package econovation.moongtaengi.study.infra.s3.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "aws.s3")
public class S3Properties {
    /**
     * S3 버킷 이름
     */
    @NotBlank(message = "S3 버킷 이름은 필수입니다")
    private String bucketName;

    /**
     * AWS 리전
     */
    @NotBlank(message = "AWS 리전은 필수입니다")
    private String region;

    /**
     * Presigned URL 만료 시간 (초)
     */
    @NotNull(message = "Presigned URL 만료 시간은 필수입니다")
    private int presignedUrlExpiration;

    /**
     * 최대 파일 크기 (bytes)
     */
    @NotNull(message = "최대 파일 크기는 필수입니다")
    private long maxFileSize;

    /**
     * 허용된 Content-Type 목록
     */
    @NotEmpty(message = "허용된 Content-Type 목록은 필수입니다")
    private List<String> allowedContentTypes;
}
