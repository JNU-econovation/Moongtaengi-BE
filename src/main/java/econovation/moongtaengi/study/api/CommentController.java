package econovation.moongtaengi.study.api;

import econovation.moongtaengi.global.annotation.LoginMemberId;
import econovation.moongtaengi.study.api.dto.CommentCreateRequest;
import econovation.moongtaengi.study.application.comment.CreateCommentCommand;
import econovation.moongtaengi.study.application.comment.CreateCommentService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CreateCommentService createCommentService;

    @PostMapping("/submissions/{submissionId}/comments")
    public ResponseEntity<Void> createComment(
            @LoginMemberId Long memberId,
            @PathVariable Long submissionId,
            @RequestBody CommentCreateRequest request
    ) {
        CreateCommentCommand command = request.toCommand(memberId, submissionId);

        Long commentId = createCommentService.createComment(command);

        return ResponseEntity.created(URI.create("/api/comments/" + commentId)).build();
    }
}
