package econovation.moongtaengi.study.api;

import econovation.moongtaengi.global.annotation.LoginMemberId;
import econovation.moongtaengi.study.api.dto.AssignmentCreateRequest;
import econovation.moongtaengi.study.application.assignment.ApproveAssignmentService;
import econovation.moongtaengi.study.application.assignment.AssignmentDetail;
import econovation.moongtaengi.study.application.assignment.AssignmentQueryService;
import econovation.moongtaengi.study.application.assignment.AssignmentSummary;
import econovation.moongtaengi.study.application.assignment.CreateAssignmentCommand;
import econovation.moongtaengi.study.application.assignment.CreateAssignmentService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final CreateAssignmentService createAssignmentService;
    private final AssignmentQueryService assignmentQueryService;
    private final ApproveAssignmentService approveAssignmentService;

    @PostMapping
    public ResponseEntity<Void> createAssignment(
            @LoginMemberId Long requesterId,
            @RequestBody @Valid AssignmentCreateRequest assignmentCreateRequest
    ) {
        log.info("과제 생성 API 호출 - requesterId: {}, processId: {}, assigneeId: {}",
                requesterId,
                assignmentCreateRequest.processId(),
                assignmentCreateRequest.assigneeId()
        );

        CreateAssignmentCommand command = assignmentCreateRequest.toCommand(requesterId);

        Long assignmentId = createAssignmentService.createAssignment(command);

        return ResponseEntity.created(URI.create("/api/assignments/" + assignmentId))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<AssignmentSummary>> getAssignmentSummaries(
            @LoginMemberId Long memberId,
            @RequestParam Long processId
    ) {
        log.info("과제 목록 조회 API 호출 - requesterId: {}, processId: {}", memberId, processId);

        List<AssignmentSummary> response = assignmentQueryService.getAssignmentSummaries(memberId, processId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{assignmentId}")
    public ResponseEntity<AssignmentDetail> getAssignmentDetail(
            @LoginMemberId Long memberId,
            @PathVariable Long assignmentId
    ) {
        log.info("과제 상세 조회 API 호출 - requesterId: {}, assignmentId: {}", memberId, assignmentId);

        AssignmentDetail response = assignmentQueryService.getAssignmentDetail(memberId, assignmentId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{assignmentId}/approve")
    public ResponseEntity<Void> approveAssignment(
            @LoginMemberId Long memberId,
            @PathVariable Long assignmentId
    ) {
        log.info("과제 승인 API 호출 - requesterId: {}, assignmentId: {}", memberId, assignmentId);

        approveAssignmentService.approveAssignment(assignmentId, memberId);
        return ResponseEntity.ok().build();
    }
}
