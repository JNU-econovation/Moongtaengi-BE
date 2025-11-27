package econovation.moongtaengi.global.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtConfig(
        String secret,
        Long accessTokenValidity
) {
}
