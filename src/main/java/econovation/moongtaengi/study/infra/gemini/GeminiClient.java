package econovation.moongtaengi.study.infra.gemini;

import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.Client;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import econovation.moongtaengi.gemini.config.GeminiConfig;
import econovation.moongtaengi.study.api.dto.GeminiProcessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Slf4j
@Component
public class GeminiClient {

    private final Client client;
    private final GeminiConfig geminiConfig;
    private final Gson gson = new Gson();

    public GeminiClient(GeminiConfig geminiConfig) {
        this.geminiConfig = geminiConfig;
        this.client = Client.builder()
                .apiKey(geminiConfig.apiKey())
                .build();

        log.info("✅ Gemini Client 초기화 완료 - model: {}", geminiConfig.model());
    }

    public GeminiProcessResponse generateProcesses(
            String studyTopic,
            LocalDate startDate,
            LocalDate endDate,
            String additionalDescription
    ) {
        int retries = 0;
        Exception lastException = null;

        while (retries < geminiConfig.maxRetries()) {
            try {
                log.info("Gemini API 호출 시도 {}/{}", retries + 1, geminiConfig.maxRetries());

                String prompt = buildPrompt(studyTopic, startDate, endDate, additionalDescription);
                String responseText = callGeminiApi(prompt);

                GeminiProcessResponse response = parseResponse(responseText);
                validateResponse(response, startDate, endDate);

                log.info("✅ Gemini API 호출 성공 - 프로세스 개수: {}", response.processes().size());
                return response;

            } catch (GeminiApiException e) {
                lastException = e;
                retries++;
                log.warn("❌ Gemini API 호출 실패 ({}/{}): {}",
                        retries, geminiConfig.maxRetries(), e.getMessage());

                if (retries < geminiConfig.maxRetries()) {
                    sleep(1000 * retries);
                }
            } catch (Exception e) {
                lastException = e;
                retries++;
                log.warn("❌ 예상치 못한 오류 ({}/{}): {}",
                        retries, geminiConfig.maxRetries(), e.getMessage());

                if (retries < geminiConfig.maxRetries()) {
                    sleep(1000 * retries);
                }
            }
        }

        log.error("❌ Gemini API 호출 최종 실패 - {} 회 재시도 실패", geminiConfig.maxRetries());
        throw new GeminiApiException(GeminiErrorCode.GEMINI_GENERATION_FAILED, lastException);
    }

    private String callGeminiApi(String prompt) {
        try {
            GenerateContentConfig config = GenerateContentConfig.builder()
                    .responseMimeType("application/json")
                    .responseSchema(ProcessSchema.getResponseSchema())
                    .build();

            GenerateContentResponse response = client.models.generateContent(
                    geminiConfig.model(),
                    prompt,
                    config
            );

            String responseText = response.text();

            if (responseText == null || responseText.isBlank()) {
                throw new GeminiApiException(GeminiErrorCode.GEMINI_GENERATION_FAILED);
            }

            log.debug("Gemini 응답: {}", responseText);
            return responseText;

        } catch (GeminiApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("Gemini API 호출 중 오류: {}", e.getMessage());
            throw new GeminiApiException(GeminiErrorCode.GEMINI_GENERATION_FAILED, e);
        }
    }

    private String buildPrompt(
            String studyTopic,
            LocalDate startDate,
            LocalDate endDate,
            String additionalDescription
    ) {
        long totalDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;

        return String.format("""
            당신은 학습 계획을 설계하는 전문가입니다.
            
            다음 스터디 정보를 바탕으로 학습 프로세스를 생성해주세요:
            
            **스터디 정보:**
            - 주제: %s
            - 기간: %d일 (%s ~ %s)
            - 추가 설명: %s
            
            **요구사항:**
            1. 최소 %d개 이상의 프로세스로 나눠주세요
            2. 각 프로세스는 기초부터 심화 순서로 배치해주세요
            3. 모든 프로세스 기간의 합은 정확히 %d일이어야 합니다
            4. 각 프로세스는 최소 3일 이상이어야 합니다
            5. 프로세스는 겹치지 않고 연속적이어야 합니다
            6. 각 프로세스의 topic은 해당 프로세스에서 학습할 핵심 주제 1개만 적어주세요
            7. assignmentDescription은 해당 프로세스에서 수행할 과제를 마크다운 형식으로 한 줄로 작성해주세요
            
            **응답 형식:**
            반드시 아래 JSON 형식으로만 응답하세요. 다른 텍스트나 설명을 포함하지 마세요.
            
            {
              "processes": [
                {
                  "order": 1,
                  "title": "프로세스 제목",
                  "durationDays": 5,
                  "topic": "학습할 주제",
                  "assignmentDescription": "과제 설명"
                }
              ]
            }
            """,
                studyTopic,
                totalDays,
                startDate,
                endDate,
                additionalDescription,
                geminiConfig.minProcesses(),
                totalDays
        );
    }

    private GeminiProcessResponse parseResponse(String responseText) {
        try {
            // JSON 정리
            String cleanJson = responseText
                    .replaceAll("```json\\s*", "")
                    .replaceAll("```\\s*", "")
                    .trim();

            log.debug("파싱할 JSON: {}", cleanJson);

            return gson.fromJson(cleanJson, GeminiProcessResponse.class);

        } catch (JsonSyntaxException e) {
            log.error("Gemini 응답 파싱 실패: {}", responseText);
            throw new GeminiApiException(GeminiErrorCode.GEMINI_RESPONSE_PARSING_FAILED, e);
        } catch (Exception e) {
            log.error("응답 파싱 중 예상치 못한 오류: {}", e.getMessage());
            throw new GeminiApiException(GeminiErrorCode.GEMINI_RESPONSE_PARSING_FAILED, e);
        }
    }

    private void validateResponse(
            GeminiProcessResponse response,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (response == null || response.processes() == null || response.processes().isEmpty()) {
            throw new GeminiApiException(GeminiErrorCode.GEMINI_RESPONSE_VALIDATION_FAILED);
        }

        // 최소 개수 검증
        if (response.processes().size() < geminiConfig.minProcesses()) {
            log.error("프로세스 개수 부족 - 최소: {}, 실제: {}",
                    geminiConfig.minProcesses(), response.processes().size());
            throw new GeminiApiException(GeminiErrorCode.GEMINI_RESPONSE_VALIDATION_FAILED);
        }

        // 총 기간 검증
        long expectedDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        long actualDays = response.processes().stream()
                .mapToLong(p -> p.durationDays())
                .sum();

        if (actualDays != expectedDays) {
            log.error("프로세스 기간 불일치 - 예상: {}일, 실제: {}일", expectedDays, actualDays);
            throw new GeminiApiException(GeminiErrorCode.GEMINI_RESPONSE_VALIDATION_FAILED);
        }

        // 각 프로세스 검증
        for (var process : response.processes()) {
            if (process.durationDays() < 3) {
                log.error("프로세스 기간 부족 - order: {}, 기간: {}일",
                        process.order(), process.durationDays());
                throw new GeminiApiException(GeminiErrorCode.GEMINI_RESPONSE_VALIDATION_FAILED);
            }
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
