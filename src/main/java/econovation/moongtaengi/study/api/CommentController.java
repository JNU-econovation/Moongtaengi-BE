package econovation.moongtaengi.study.api;

import econovation.moongtaengi.global.annotation.LoginMemberId;
import econovation.moongtaengi.study.api.dto.CommentCreateRequest;
import econovation.moongtaengi.study.api.dto.CommentUpdateRequest;
import econovation.moongtaengi.study.application.comment.CommentQueryService;
import econovation.moongtaengi.study.application.comment.CommentSummary;
import econovation.moongtaengi.study.application.comment.CreateCommentCommand;
import econovation.moongtaengi.study.application.comment.CreateCommentService;
import econovation.moongtaengi.study.application.comment.UpdateCommentService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CreateCommentService createCommentService;
    private final UpdateCommentService updateCommentService;
    private final CommentQueryService commentQueryService;

    @PostMapping("/submissions/{submissionId}/comments")
    public ResponseEntity<Void> createComment(
            @LoginMemberId Long memberId,
            @PathVariable Long submissionId,
            @RequestBody @Valid CommentCreateRequest request
    ) {
        log.info("댓글 생성 API 호출 - memberId: {}, submissionId: {}", memberId, submissionId);

        CreateCommentCommand command = request.toCommand(memberId, submissionId);

        Long commentId = createCommentService.createComment(command);

        return ResponseEntity.created(URI.create("/api/comments/" + commentId)).build();
    }

    @GetMapping("/submissions/{submissionId}/comments")
    public ResponseEntity<List<CommentSummary>> getComments(
            @PathVariable Long submissionId,
            @LoginMemberId Long memberId
    ) {
        List<CommentSummary> response = commentQueryService.getComments(submissionId, memberId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Void> updateComment(
            @LoginMemberId Long memberId,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentUpdateRequest request
    ) {
        log.info("댓글 수정 API 호출 - memberId: {}, commentId: {}", memberId, commentId);

        updateCommentService.updateComment(request.toCommand(commentId, memberId));

        return ResponseEntity.ok().build();
    }
}
