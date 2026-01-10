package econovation.moongtaengi.admin.api;

import econovation.moongtaengi.admin.api.dto.AdminStudyResponse;
import econovation.moongtaengi.admin.api.dto.CreateStudyRequest;
import econovation.moongtaengi.admin.application.AdminStudyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자 - 스터디 관리 API
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/studies")
@RequiredArgsConstructor
public class AdminStudyController {

    private final AdminStudyService adminStudyService;

    /**
     * 스터디 목록 조회
     * GET /api/admin/studies
     */
    @GetMapping
    public ResponseEntity<Page<AdminStudyResponse>> getStudies(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        log.info("스터디 목록 조회 API 호출");

        Page<AdminStudyResponse> studies = adminStudyService.getStudies(pageable);

        log.info("✅ 스터디 목록 조회 API 완료 - 총 {}개", studies.getTotalElements());

        return ResponseEntity.ok(studies);
    }

    /**
     * 스터디 상세 조회
     * GET /api/admin/studies/{studyId}
     */
    @GetMapping("/{studyId}")
    public ResponseEntity<AdminStudyResponse> getStudy(
            @PathVariable Long studyId
    ) {
        log.info("스터디 상세 조회 API 호출 - studyId: {}", studyId);

        AdminStudyResponse study = adminStudyService.getStudy(studyId);

        log.info("✅ 스터디 상세 조회 API 완료");

        return ResponseEntity.ok(study);
    }

    /**
     * 스터디 생성
     * POST /api/admin/studies
     */
    @PostMapping
    public ResponseEntity<AdminStudyResponse> createStudy(
            @Valid @RequestBody CreateStudyRequest request
    ) {
        log.info("스터디 생성 API 호출 - name: {}", request.name());

        AdminStudyResponse study = adminStudyService.createStudy(request);

        log.info("✅ 스터디 생성 API 완료 - studyId: {}", study.id());

        return ResponseEntity.ok(study);
    }

    /**
     * 스터디 삭제
     * DELETE /api/admin/studies/{studyId}
     */
    @DeleteMapping("/{studyId}")
    public ResponseEntity<Void> deleteStudy(
            @PathVariable Long studyId
    ) {
        log.info("스터디 삭제 API 호출 - studyId: {}", studyId);

        adminStudyService.deleteStudy(studyId);

        log.info("✅ 스터디 삭제 API 완료");

        return ResponseEntity.ok().build();
    }
}
