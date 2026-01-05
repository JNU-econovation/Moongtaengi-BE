package econovation.moongtaengi.study.api.dto;

import java.util.List;

public record GeminiProcessResponse(
        List<ProcessInfo> processes
) {
    public record ProcessInfo(
            Integer order,
            String title,
            Integer durationDays,
            String assignmentDescription
    ) {
    }
}
