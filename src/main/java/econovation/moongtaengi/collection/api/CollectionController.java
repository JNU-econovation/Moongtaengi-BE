package econovation.moongtaengi.collection.api;

import econovation.moongtaengi.collection.api.dto.CollectionInfo;
import econovation.moongtaengi.collection.api.dto.CollectionResponse;
import econovation.moongtaengi.collection.api.dto.EquipCollectionRequest;
import econovation.moongtaengi.collection.application.CollectionService;
import econovation.moongtaengi.collection.domain.CollectionType;
import econovation.moongtaengi.global.annotation.LoginMemberId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 컬렉션 API 컨트롤러
 * 컬렉션 조회 및 장착 기능 제공
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/collections")
public class CollectionController {

    private final CollectionService collectionService;

    /**
     * 내 컬렉션 목록 조회
     * @param memberId 로그인한 회원 ID
     * @return 컬렉션 목록 응답
     */
    @GetMapping
    public ResponseEntity<CollectionResponse> getCollections(@LoginMemberId Long memberId) {
        log.info("컬렉션 목록 조회 API 호출 - memberId: {}", memberId);

        CollectionResponse response = collectionService.getCollections(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{type}")
    public ResponseEntity<CollectionInfo> getCollection(@LoginMemberId Long memberId, @PathVariable("type")
            CollectionType type) {
        log.info("컬렉션 단건 조회 API 호출 - memberId: {}, collectionType: {}", memberId, type);

        CollectionInfo response = collectionService.getCollectionDetails(memberId, type);
        return ResponseEntity.ok(response);
    }

    /**
     * 프로필 아이콘 변경 (컬렉션 장착)
     * @param memberId 로그인한 회원 ID
     * @param request 장착할 컬렉션 요청
     * @return 성공 응답
     */
    @PostMapping("/equip")
    public ResponseEntity<Void> equipCollection(
            @LoginMemberId Long memberId,
            @RequestBody @Valid EquipCollectionRequest request) {
        log.info("컬렉션 장착 API 호출 - memberId: {}, collectionType: {}",
                memberId, request.collectionType());

        collectionService.equipCollection(memberId, request.collectionType());
        return ResponseEntity.ok().build();
    }
}
