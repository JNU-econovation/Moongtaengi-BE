package econovation.moongtaengi.study.infra.s3.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "aws.credentials")
public class AwsCredentialsProperties {
    /**
     * AWS Access Key
     */
    private String accessKey;

    /**
     * AWS Secret Key
     */
    private String secretKey;
}
