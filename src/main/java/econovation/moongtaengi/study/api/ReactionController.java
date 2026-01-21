package econovation.moongtaengi.study.api;

import econovation.moongtaengi.global.annotation.LoginMemberId;
import econovation.moongtaengi.study.api.dto.ReactionToggleRequest;
import econovation.moongtaengi.study.application.reaction.ToggleReactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReactionController {

    private final ToggleReactionService toggleReactionService;

    @PostMapping("/submissions/{submissionId}/reactions")
    public ResponseEntity<Void> toggleReaction(
            @LoginMemberId Long memberId,
            @PathVariable Long submissionId,
            @RequestBody @Valid ReactionToggleRequest reactionToggleRequest
    ) {
        log.info("감정표현 토글 API 호출 - memberId: {}, submissionId: {}, type: {}",
                memberId, submissionId, reactionToggleRequest.emojiType());

        toggleReactionService.toggleReaction(
                submissionId,
                memberId,
                reactionToggleRequest.emojiType()
        );

        return ResponseEntity.ok().build();
    }
}
