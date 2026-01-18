package econovation.moongtaengi.collection.domain.event;

import econovation.moongtaengi.collection.domain.CollectionType;

/**
 * 컬렉션 장착 이벤트
 * 프로필 아이콘이 변경될 때 발행되어 온보딩 미션 진행에 사용
 */
public record CollectionEquippedEvent(
        Long memberId,
        CollectionType collectionType
) {
}