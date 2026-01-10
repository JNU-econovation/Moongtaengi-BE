package econovation.moongtaengi.study.infra.s3.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "aws.credentials")
public class AwsCredentialsProperties {
    /**
     * AWS Access Key
     */
    @NotBlank(message = "AWS Access Key는 필수입니다")
    private String accessKey;

    /**
     * AWS Secret Key
     */
    @NotBlank(message = "AWS Secret Key는 필수입니다")
    private String secretKey;
}
