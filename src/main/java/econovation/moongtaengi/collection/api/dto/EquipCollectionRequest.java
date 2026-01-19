package econovation.moongtaengi.collection.api.dto;

import econovation.moongtaengi.collection.domain.CollectionType;
import jakarta.validation.constraints.NotNull;

/**
 * 컬렉션 장착 요청 DTO
 */
public record EquipCollectionRequest(
        @NotNull(message = "컬렉션 타입은 필수입니다.")
        CollectionType collectionType
) {
}