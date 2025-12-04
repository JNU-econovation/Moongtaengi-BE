package econovation.moongtaengi.member.infra.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfoResponse(
        @JsonProperty("id")
        Long id
) {
}
