package econovation.moongtaengi.member.infrastructure.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfoResponse(
        @JsonProperty("id")
        Long id
) {
}
