package econovation.moongtaengi.collection.api.dto;

import econovation.moongtaengi.collection.domain.CollectionRarity;
import econovation.moongtaengi.collection.domain.CollectionType;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 컬렉션 목록 조회 응답 DTO
 */
public record CollectionResponse(
        CollectionType equippedIcon,
        List<CollectionInfo> collections
) {
    /**
     * 개별 컬렉션 정보
     */
    public record CollectionInfo(
            CollectionType type,
            String displayName,
            CollectionRarity rarity,
            String description,
            boolean unlocked,
            LocalDateTime unlockedAt,
            String imageUrl
    ) {
    }
}