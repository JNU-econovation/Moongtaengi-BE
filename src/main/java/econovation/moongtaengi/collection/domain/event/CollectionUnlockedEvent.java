package econovation.moongtaengi.collection.domain.event;

import econovation.moongtaengi.collection.domain.CollectionType;

/**
 * 컬렉션 해금 이벤트
 * 컬렉션이 해금될 때 발행되어 다른 도메인(알림, 온보딩)에서 처리
 */
public record CollectionUnlockedEvent(
        Long memberId,
        CollectionType collectionType
) {
}