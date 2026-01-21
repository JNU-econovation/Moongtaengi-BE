package econovation.moongtaengi.study.domain.reaction;

/**
 * 감정표현 추가 이벤트
 */
public record ReactionAddedEvent(
        Long reactionId,
        Long submissionId,
        Long memberId,
        EmojiType emojiType
) {
}