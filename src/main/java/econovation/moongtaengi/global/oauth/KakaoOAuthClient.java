package econovation.moongtaengi.global.oauth;

import econovation.moongtaengi.global.exception.KakaoAuthException;
import econovation.moongtaengi.global.exception.KakaoServerException;
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

@Component
public class KakaoOAuthClient {

    private static final int CONNECTION_TIMEOUT_MS = 5000;  // 5초
    private static final int READ_TIMEOUT_MS = 10000;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;

    private final RestClient restClient;

    public KakaoOAuthClient() {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofMillis(CONNECTION_TIMEOUT_MS));
        requestFactory.setReadTimeout(Duration.ofMillis(READ_TIMEOUT_MS));

        this.restClient = RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }


    public String getAccessToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientId);
        formData.add("redirect_uri", redirectUri);
        formData.add("code", code);

        try {
            return restClient.post()
                    .uri(tokenUri)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new KakaoAuthException();
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new KakaoServerException();
                    })
                    .body(String.class);
        } catch (KakaoAuthException | KakaoServerException e) {
            throw new KakaoAuthException(e);
        }
    }


    public String getKakaoId(String accessToken) {
        try {
            return restClient.get()
                    .uri(userInfoUri)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new KakaoAuthException();
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new KakaoServerException();
                    })
                    .body(String.class);
        } catch (KakaoAuthException | KakaoServerException e) {
            throw new KakaoAuthException(e);
        }
    }
}
