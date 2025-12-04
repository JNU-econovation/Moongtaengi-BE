package econovation.moongtaengi.member.infra.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.kakao")
public record KakaoConfig(
        String clientId,
        String redirectUri,
        String tokenUri,
        String userInfoUri
) {
}
