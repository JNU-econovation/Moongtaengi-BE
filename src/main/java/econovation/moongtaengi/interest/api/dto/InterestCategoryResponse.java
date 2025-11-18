package econovation.moongtaengi.interest.api.dto;

import econovation.moongtaengi.interest.domain.InterestCategory;

public record InterestCategoryResponse(
        Long id,
        String displayName
) {
    public static InterestCategoryResponse from(InterestCategory category) {
        return new InterestCategoryResponse(
                category.getId(),
                category.getDisplayName()
        );
    }
}
