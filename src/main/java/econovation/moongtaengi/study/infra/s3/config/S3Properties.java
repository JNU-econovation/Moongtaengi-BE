package econovation.moongtaengi.study.infra.s3.config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "aws.s3")
public class S3Properties {
    /**
     * S3 버킷 이름
     */
    private String bucketName;

    /**
     * AWS 리전
     */
    private String region;

    /**
     * Presigned URL 만료 시간 (초)
     */
    private int presignedUrlExpiration;

    /**
     * 최대 파일 크기 (bytes)
     */
    private long maxFileSize;

    /**
     * 허용된 Content-Type 목록
     */
    private List<String> allowedContentTypes;
}
