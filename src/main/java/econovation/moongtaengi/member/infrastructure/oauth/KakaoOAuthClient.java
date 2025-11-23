package econovation.moongtaengi.member.infrastructure.oauth;

import econovation.moongtaengi.global.exception.KakaoAuthException;
import econovation.moongtaengi.global.exception.KakaoServerException;
import econovation.moongtaengi.member.infrastructure.oauth.config.KakaoConfig;
import econovation.moongtaengi.member.infrastructure.oauth.dto.KakaoTokenResponse;
import econovation.moongtaengi.member.infrastructure.oauth.dto.KakaoUserInfoResponse;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class KakaoOAuthClient {

    private static final int CONNECTION_TIMEOUT_MS = 5000;  // 5초
    private static final int READ_TIMEOUT_MS = 10000;

    private final KakaoConfig kakaoConfig;
    private final RestClient restClient;

    public KakaoOAuthClient(KakaoConfig kakaoConfig) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofMillis(CONNECTION_TIMEOUT_MS));
        requestFactory.setReadTimeout(Duration.ofMillis(READ_TIMEOUT_MS));

        this.kakaoConfig = kakaoConfig;

        this.restClient = RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }

    public String getAccessToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", kakaoConfig.clientId());
        formData.add("redirect_uri", kakaoConfig.redirectUri());
        formData.add("code", code);

        try {
            KakaoTokenResponse response = restClient.post()
                    .uri(kakaoConfig.tokenUri())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, res) -> {
                        throw new KakaoAuthException();
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, res) -> {
                        throw new KakaoServerException();
                    })
                    .body(KakaoTokenResponse.class);

            if (response == null || response.accessToken() == null) {
                throw new KakaoAuthException();
            }

            return response.accessToken();

        } catch (KakaoAuthException | KakaoServerException e) {
            throw new KakaoAuthException(e);
        }
    }

    public String getKakaoId(String accessToken) {
        try {
            KakaoUserInfoResponse response = restClient.get()
                    .uri(kakaoConfig.userInfoUri())
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, res) -> {
                        throw new KakaoAuthException();
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, res) -> {
                        throw new KakaoServerException();
                    })
                    .body(KakaoUserInfoResponse.class);

            if (response == null || response.id() == null) {
                throw new KakaoAuthException();
            }

            return String.valueOf(response.id());

        } catch (KakaoAuthException | KakaoServerException e) {
            throw new KakaoAuthException(e);
        }
    }
}
