package econovation.moongtaengi.study.domain.reaction;

public record ReactionStat(
        EmojiType emojiType,
        Long count,
        boolean isClicked
) {
    public ReactionStat(EmojiType emojiType, Long count, Long myCount) {
        this(emojiType, count, myCount != null && myCount > 0);
    }
}
