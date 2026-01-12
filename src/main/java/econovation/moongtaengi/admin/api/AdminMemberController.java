package econovation.moongtaengi.admin.api;

import econovation.moongtaengi.admin.api.dto.AdminMemberResponse;
import econovation.moongtaengi.admin.api.dto.CreateMemberRequest;
import econovation.moongtaengi.admin.application.AdminMemberService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자 - 회원 관리 API
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    /**
     * 회원 목록 조회
     * GET /api/admin/members
     */
    @GetMapping
    public ResponseEntity<Page<AdminMemberResponse>> getMembers(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        log.info("회원 목록 조회 API 호출");

        Page<AdminMemberResponse> members = adminMemberService.getMembers(pageable);

        log.info("✅ 회원 목록 조회 API 완료 - 총 {}개", members.getTotalElements());

        return ResponseEntity.ok(members);
    }

    /**
     * 회원 상세 조회
     * GET /api/admin/members/{memberId}
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<AdminMemberResponse> getMember(
            @PathVariable Long memberId
    ) {
        log.info("회원 상세 조회 API 호출 - memberId: {}", memberId);

        AdminMemberResponse member = adminMemberService.getMember(memberId);

        log.info("✅ 회원 상세 조회 API 완료");

        return ResponseEntity.ok(member);
    }

    /**
     * 회원 생성
     * POST /api/admin/members
     */
    @PostMapping
    public ResponseEntity<AdminMemberResponse> createMember(
            @Valid @RequestBody CreateMemberRequest request
    ) {
        log.info("회원 생성 API 호출 - kakaoId: {}", request.kakaoId());

        AdminMemberResponse member = adminMemberService.createMember(request);

        log.info("✅ 회원 생성 API 완료 - memberId: {}", member.id());

        return ResponseEntity.ok(member);
    }

    /**
     * 회원 삭제
     * DELETE /api/admin/members/{memberId}
     */
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(
            @PathVariable Long memberId
    ) {
        log.info("회원 삭제 API 호출 - memberId: {}", memberId);

        adminMemberService.deleteMember(memberId);

        log.info("✅ 회원 삭제 API 완료");

        return ResponseEntity.ok().build();
    }

    /**
     * Mock 회원 10명 생성
     * POST /api/admin/members/mock
     */
    @PostMapping("/mock")
    public ResponseEntity<Map<String, Integer>> createMockMembers() {
        log.info("Mock 회원 생성 API 호출");

        int createdCount = adminMemberService.createMockMembers();

        log.info("✅ Mock 회원 생성 API 완료 - {}명", createdCount);

        return ResponseEntity.ok(Map.of("createdCount", createdCount));
    }
}
