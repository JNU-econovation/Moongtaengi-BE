package econovation.moongtaengi.study.api;

import econovation.moongtaengi.global.annotation.LoginMemberId;
import econovation.moongtaengi.study.api.dto.SubmissionCreateRequest;
import econovation.moongtaengi.study.application.submission.CreateSubmissionCommand;
import econovation.moongtaengi.study.application.submission.CreateSubmissionService;
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
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final CreateSubmissionService createSubmissionService;

    @PostMapping
    public ResponseEntity<Void> createSubmission(
            @LoginMemberId Long submitterId,
            @RequestBody @Valid SubmissionCreateRequest request
    ) {
        CreateSubmissionCommand command = request.toCommand(submitterId);

        Long submissionId = createSubmissionService.createSubmission(command);

        return ResponseEntity.created(URI.create("/api/submissions/" + submissionId)).build();
    }

}
