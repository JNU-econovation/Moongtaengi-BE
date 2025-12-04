package econovation.moongtaengi.study.infra.gemini;

import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.Part;
import com.google.gson.Gson;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import econovation.moongtaengi.gemini.config.GeminiConfig;
import econovation.moongtaengi.study.api.dto.GeminiProcessResponse;
import econovation.moongtaengi.study.api.dto.GeminiProcessResponse.ProcessInfo;
import java.util.List;
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

            } catch (Exception e) {
                lastException = e;
                retries++;
                log.warn("❌ Gemini API 호출 실패 ({}/{}): {}",
                        retries, geminiConfig.maxRetries(), e.getMessage());

                if (retries < geminiConfig.maxRetries()) {
                    sleep(1000 * retries);
                }
            }
        }

        log.error("❌ Gemini API 호출 최종 실패");
        throw new GeminiApiException("Gemini API 호출에 실패했습니다. 잠시 후 다시 시도해주세요.", lastException);
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
            
            다음 조건에 맞는 학습 프로세스를 생성해주세요:
            
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
            
            **응답 형식 (JSON):**
```json
            {
              "processes": [
                {
                  "order": 1,
                  "title": "프로세스 제목",
                  "durationDays": 5,
                  "topic": "학습할 주제 (1개만)",
                  "assignmentDescription": "과제 한줄 설명 (마크다운 형식)"
                }
              ]
            }
```
            
            **중요:**
            - 반드시 유효한 JSON 형식으로만 응답하세요
            - JSON 외에 다른 텍스트를 포함하지 마세요
            - 코드 블록(```)을 사용하지 마세요
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

    private String callGeminiApi(String prompt) {
        try {
            Content content = new Content.Builder()
                    .addPart(new Part.Builder().text(prompt).build())
                    .build();

            GenerateContentConfig config = new GenerateContentConfig.Builder()
                    .temperature(0.7)
                    .maxOutputTokens(2048)
                    .responseMimeType("application/json")  // ✅ JSON 응답 강제
                    .responseSchema(ProcessSchema.getResponseSchema())  // ✅ Schema 적용
                    .build();

            GenerateContentResponse response = client
                    .models()
                    .generateContent(
                            geminiConfig.model(),
                            List.of(content),
                            config
                    )
                    .get();

            String responseText = extractText(response);

            if (response == null || response.getText() == null) {
                throw new GeminiApiException("Gemini API 응답이 비어있습니다");
            }

            return response.getText();

        } catch (Exception e) {
            throw new GeminiApiException("Gemini API 호출 중 오류 발생", e);
        }
    }

    private String extractText(GenerateContentResponse response) {
        if (response == null) {
            throw new GeminiApiException("Gemini 응답이 null입니다");
        }

        if (response.getCandidates() == null || response.getCandidates().isEmpty()) {
            throw new GeminiApiException("Gemini 응답에 candidates가 없습니다");
        }

        var candidate = response.getCandidates().get(0);

        if (candidate.getContent() == null) {
            throw new GeminiApiException("Gemini 응답에 content가 없습니다");
        }

        if (candidate.getContent().getParts() == null ||
                candidate.getContent().getParts().isEmpty()) {
            throw new GeminiApiException("Gemini 응답에 parts가 없습니다");
        }

        var part = candidate.getContent().getParts().get(0);
        String text = part.getText();

        if (text == null || text.isBlank()) {
            throw new GeminiApiException("Gemini 응답에 텍스트가 없습니다");
        }

        return text;
    }


    private void validateResponse(GeminiProcessResponse response, LocalDate startDate, LocalDate endDate) {
        if (response == null || response.processes() == null || response.processes().isEmpty()) {
            throw new GeminiApiException("Gemini가 프로세스를 생성하지 못했습니다");
        }

        if (response.processes().size() < geminiConfig.minProcesses()) {
            throw new GeminiApiException(
                    String.format("프로세스는 최소 %d개 이상이어야 합니다", geminiConfig.minProcesses())
            );
        }

        long expectedDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        long actualDays = response.processes().stream()
                .mapToLong(ProcessInfo::durationDays)
                .sum();

        if (actualDays != expectedDays) {
            throw new GeminiApiException(
                    String.format("프로세스 기간 합(%d일)이 스터디 기간(%d일)과 일치하지 않습니다",
                            actualDays, expectedDays)
            );
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
