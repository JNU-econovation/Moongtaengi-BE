package econovation.moongtaengi.gemini.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gemini")
public record GeminiConfig(
        String apiKey,
        String model,
        Integer maxRetries,
        Integer minProcesses
) {
}
