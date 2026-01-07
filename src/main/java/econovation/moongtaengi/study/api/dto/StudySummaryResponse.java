package econovation.moongtaengi.study.api.dto;

import econovation.moongtaengi.study.domain.Study;

public record StudySummaryResponse(
        Long id,
        String name
) {
    public static StudySummaryResponse from(Study study) {
        return new StudySummaryResponse(
                study.getId(),
                study.getName().getValue()
        );
    }
}
