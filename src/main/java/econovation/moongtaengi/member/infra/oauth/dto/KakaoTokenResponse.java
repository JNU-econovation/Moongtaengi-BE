package econovation.moongtaengi.member.infra.oauth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoTokenResponse(
        String accessToken,
        String tokenType,
        String refreshToken,
        Integer expiresIn,
        String scope,
        Integer refreshTokenExpiresIn
) {
}
