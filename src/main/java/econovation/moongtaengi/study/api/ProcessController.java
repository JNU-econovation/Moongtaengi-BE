package econovation.moongtaengi.study.api;

import econovation.moongtaengi.global.annotation.LoginMemberId;
import econovation.moongtaengi.study.api.dto.BatchProcessRequest;
import econovation.moongtaengi.study.api.dto.ProcessResponse;
import econovation.moongtaengi.study.application.ProcessService;
import econovation.moongtaengi.study.api.dto.GenerateProcessRequest;
import jakarta.validation.Valid;
import java.util.List;
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

    @GetMapping
    public ResponseEntity<List<ProcessResponse>> getProcesses(
            @LoginMemberId Long memberId,
            @PathVariable Long studyId
    ) {
        log.info("프로세스 목록 조회 API 호출 - studyId: {}, memberId: {}", studyId, memberId);

        List<ProcessResponse> processes = processService.getProcesses(studyId, memberId);

        log.info("✅ 프로세스 목록 조회 API 완료 - studyId: {}, 개수: {}", studyId, processes.size());

        return ResponseEntity.ok(processes);
    }

    @GetMapping("/{processId}")
    public ResponseEntity<ProcessResponse> getProcess(
            @LoginMemberId Long memberId,
            @PathVariable Long studyId,
            @PathVariable Long processId
    ) {
        log.info("프로세스 단건 조회 API 호출 - studyId: {}, processId: {}", studyId, processId);

        ProcessResponse process = processService.getProcess(studyId, processId, memberId);

        log.info("✅ 프로세스 단건 조회 API 완료 - studyId: {}, processId: {}", studyId, processId);

        return ResponseEntity.ok(process);
    }

    /**
     * 프로세스 일괄 저장
     * POST /api/studies/{studyId}/processes/batch
     *
     * 권한: 호스트만
     * 기능: 추가 + 수정
     */
    @PostMapping("/batch")
    public ResponseEntity<Void> batchProcesses(
            @LoginMemberId Long memberId,
            @PathVariable Long studyId,
            @Valid @RequestBody BatchProcessRequest request
    ) {
        log.info("프로세스 일괄 저장 API 호출 - studyId: {}, memberId: {}, 개수: {}",
                studyId, memberId, request.processes().size());

        processService.batchProcesses(studyId, memberId, request);

        log.info("✅ 프로세스 일괄 저장 API 완료 - studyId: {}", studyId);

        return ResponseEntity.ok().build();
    }

    /**
     * 프로세스 삭제
     * DELETE /api/studies/{studyId}/processes/{processId}
     *
     * 권한: 호스트만
     *
     * 삭제 후 남은 프로세스의 순서를 재정렬하려면
     * 프론트에서 batch API를 추가로 호출해야 함
     */
    @DeleteMapping("/{processId}")
    public ResponseEntity<Void> deleteProcess(
            @LoginMemberId Long memberId,
            @PathVariable Long studyId,
            @PathVariable Long processId
    ) {
        log.info("프로세스 삭제 API 호출 - studyId: {}, processId: {}, memberId: {}",
                studyId, processId, memberId);

        processService.deleteProcess(studyId, processId, memberId);

        log.info("✅ 프로세스 삭제 API 완료 - studyId: {}, processId: {}", studyId, processId);

        return ResponseEntity.ok().build();
    }
}
