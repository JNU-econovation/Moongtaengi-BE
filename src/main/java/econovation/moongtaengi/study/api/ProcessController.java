package econovation.moongtaengi.study.api;

import econovation.moongtaengi.study.application.ProcessService;
import econovation.moongtaengi.study.api.dto.GenerateProcessRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 프로세스 관련 REST API Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/studies/{studyId}/processes")
@RequiredArgsConstructor
public class ProcessController {

    private final ProcessService processService;

    /**
     * 프로세스 생성 (Gemini 호출)
     * POST /api/studies/{studyId}/processes
     *
     * Authorization: Bearer {JWT}
     * Body: { "additionalDescription": "기초부터 심화까지 체계적으로 학습" }
     */
    @PostMapping("/generate")
    public ResponseEntity<Void> generateProcesses(
            @PathVariable Long studyId,
            @Valid @RequestBody GenerateProcessRequest request
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = (Long) auth.getPrincipal();

        log.info("프로세스 생성 API 호출 - studyId: {}, memberId: {}", studyId, memberId);

        // ✅ Service 호출
        processService.generateProcesses(studyId, memberId, request.additionalDescription());

        log.info("✅ 프로세스 생성 API 완료 - studyId: {}", studyId);

        return ResponseEntity.ok().build();
    }
}
