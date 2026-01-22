package econovation.moongtaengi.study.api;

import econovation.moongtaengi.global.annotation.LoginMemberId;
import econovation.moongtaengi.study.api.dto.SubmissionCreateRequest;
import econovation.moongtaengi.study.api.dto.SubmissionUpdateRequest;
import econovation.moongtaengi.study.application.submission.CreateSubmissionCommand;
import econovation.moongtaengi.study.application.submission.CreateSubmissionService;
import econovation.moongtaengi.study.application.submission.UpdateSubmissionCommand;
import econovation.moongtaengi.study.application.submission.UpdateSubmissionService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final CreateSubmissionService createSubmissionService;
    private final UpdateSubmissionService updateSubmissionService;

    @PostMapping
    public ResponseEntity<Void> createSubmission(
            @LoginMemberId Long submitterId,
            @RequestBody @Valid SubmissionCreateRequest request
    ) {
        log.info("과제 제출 API 호출 - submitterId: {}", submitterId);

        CreateSubmissionCommand command = request.toCommand(submitterId);

        Long submissionId = createSubmissionService.createSubmission(command);

        return ResponseEntity.created(URI.create("/api/submissions/" + submissionId)).build();
    }

    @PatchMapping("/{submissionId}")
    public ResponseEntity<Void> updateSubmission(
            @LoginMemberId Long requesterId,
            @PathVariable Long submissionId,
            @RequestBody SubmissionUpdateRequest request
    ) {
        log.info("과제 제출 수정 API 호출 - requesterId: {}, submissionId: {}", requesterId, submissionId);

        UpdateSubmissionCommand command = request.toCommand(submissionId, requesterId);

        updateSubmissionService.updateSubmission(command);

        return ResponseEntity.ok().build();
    }

}
