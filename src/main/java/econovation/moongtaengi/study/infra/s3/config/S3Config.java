package econovation.moongtaengi.study.infra.s3.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class S3Config {
    private final S3Properties s3Properties;
    private final AwsCredentialsProperties credentialsProperties;

    @Bean
    public AwsCredentialsProvider credentialsProvider() {
        log.info("AWS Credentials 설정 - Access Key 방식");

        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                credentialsProperties.getAccessKey(),
                credentialsProperties.getSecretKey()
        );

        return StaticCredentialsProvider.create(credentials);
    }

    @Bean
    public S3Client s3Client(AwsCredentialsProvider credentialsProvider) {
        log.info("S3 Client 생성 - 리전: {}, 버킷: {}",
                s3Properties.getRegion(),
                s3Properties.getBucketName());

        return S3Client.builder()
                .region(Region.of(s3Properties.getRegion()))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    /**
     * S3 Presigner
     */
    @Bean
    public S3Presigner s3Presigner(AwsCredentialsProvider credentialsProvider) {
        log.info("S3 Presigner 생성 - 만료 시간: {}초",
                s3Properties.getPresignedUrlExpiration());

        return S3Presigner.builder()
                .region(Region.of(s3Properties.getRegion()))
                .credentialsProvider(credentialsProvider)
                .build();
    }
}
