package econovation.moongtaengi.collection.api.dto;

import econovation.moongtaengi.collection.domain.CollectionType;
import java.util.List;

/**
 * 컬렉션 목록 조회 응답 DTO
 */
public record CollectionResponse(
        CollectionType equippedIcon,
        List<CollectionInfo> collections
) {
}
