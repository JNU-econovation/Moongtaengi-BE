package econovation.moongtaengi.collection.application;

import econovation.moongtaengi.collection.api.dto.CollectionResponse;
import econovation.moongtaengi.collection.domain.Collection;
import econovation.moongtaengi.collection.domain.CollectionErrorCode;
import econovation.moongtaengi.collection.domain.CollectionException;
import econovation.moongtaengi.collection.domain.CollectionRepository;
import econovation.moongtaengi.collection.domain.CollectionType;
import econovation.moongtaengi.collection.domain.event.CollectionEquippedEvent;
import econovation.moongtaengi.collection.domain.event.CollectionUnlockedEvent;
import econovation.moongtaengi.member.application.MemberNotFoundException;
import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.MemberRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 컬렉션 서비스
 * 컬렉션 조회 및 장착 기능 제공
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 회원의 컬렉션 목록 조회
     * 모든 컬렉션 타입을 반환하며, 해금 여부를 표시
     * @param memberId 회원 ID
     * @return 컬렉션 목록 응답
     */
    public CollectionResponse getCollections(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        // 회원이 해금한 컬렉션 목록 조회
        List<Collection> unlockedCollections = collectionRepository.findAllByMemberId(memberId);

        // 컬렉션 타입별로 매핑 (빠른 조회를 위해)
        Map<CollectionType, Collection> unlockedMap = unlockedCollections.stream()
                .collect(Collectors.toMap(Collection::getType, c -> c));

        // 모든 컬렉션 타입에 대해 응답 생성
        List<CollectionResponse.CollectionInfo> collectionInfos = Arrays.stream(CollectionType.values())
                .map(type -> {
                    Collection collection = unlockedMap.get(type);
                    boolean unlocked = collection != null;

                    return new CollectionResponse.CollectionInfo(
                            type,
                            type.getDisplayName(),
                            type.getRarity(),
                            type.getDescription(),
                            unlocked,
                            unlocked ? collection.getUnlockedAt() : null,
                            type.getImageUrl()
                    );
                })
                .toList();

        log.info("회원 {}의 컬렉션 {} 개를 조회했습니다", memberId, collectionInfos.size());
        return new CollectionResponse(member.getProfileIcon(), collectionInfos);
    }

    /**
     * 프로필 아이콘 변경 (컬렉션 장착)
     * 보유하지 않은 컬렉션은 장착할 수 없음
     * @param memberId 회원 ID
     * @param collectionType 장착할 컬렉션 타입
     */
    @Transactional
    public void equipCollection(Long memberId, CollectionType collectionType) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        // 보유 여부 확인
        boolean hasCollection = collectionRepository.existsByMemberIdAndType(memberId, collectionType);
        if (!hasCollection) {
            log.warn("회원 {}가 보유하지 않은 컬렉션 {}을 장착하려 시도했습니다", memberId, collectionType);
            throw new CollectionException(CollectionErrorCode.COLLECTION_NOT_OWNED);
        }

        // 프로필 아이콘 변경
        member.changeProfileIcon(collectionType);
        memberRepository.save(member);

        // 이벤트 발행 (온보딩 미션 진행에 사용)
        eventPublisher.publishEvent(new CollectionEquippedEvent(memberId, collectionType));

        log.info("회원 {}가 컬렉션 {}을 장착했습니다", memberId, collectionType);
    }

    /**
     * 컬렉션 해금
     * 이미 해금된 컬렉션은 중복 해금하지 않음
     * @param memberId 회원 ID
     * @param collectionType 해금할 컬렉션 타입
     */
    @Transactional
    public void unlockCollection(Long memberId, CollectionType collectionType) {
        // 중복 해금 방지
        if (collectionRepository.existsByMemberIdAndType(memberId, collectionType)) {
            log.info("회원 {}는 이미 컬렉션 {}을 보유하고 있습니다, 해금 생략", memberId, collectionType);
            return;
        }

        // 컬렉션 해금
        Collection collection = Collection.unlock(memberId, collectionType);
        collectionRepository.save(collection);

        // 이벤트 발행 (알림, TREASURE_BAG 해금 조건 체크에 사용)
        eventPublisher.publishEvent(new CollectionUnlockedEvent(memberId, collectionType));

        log.info("회원 {}에게 컬렉션 {}이 해금되었습니다", memberId, collectionType);
    }

    /**
     * 회원이 보유한 컬렉션 개수 조회
     * @param memberId 회원 ID
     * @return 컬렉션 개수
     */
    public long getCollectionCount(Long memberId) {
        return collectionRepository.countByMemberId(memberId);
    }
}
