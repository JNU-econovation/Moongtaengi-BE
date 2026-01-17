package econovation.moongtaengi.study.api;

import econovation.moongtaengi.global.annotation.LoginMemberId;
import econovation.moongtaengi.study.api.dto.AssignmentCreateRequest;
import econovation.moongtaengi.study.application.assignment.CreateAssignmentCommand;
import econovation.moongtaengi.study.application.assignment.CreateAssignmentService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final CreateAssignmentService createAssignmentService;

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
}
