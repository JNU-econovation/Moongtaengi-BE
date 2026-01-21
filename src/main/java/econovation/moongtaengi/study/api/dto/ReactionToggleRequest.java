package econovation.moongtaengi.study.api.dto;

import econovation.moongtaengi.study.domain.reaction.EmojiType;
import jakarta.validation.constraints.NotNull;

public record ReactionToggleRequest(
        @NotNull(message = "이모지 타입은 필수입니다.")
        EmojiType emojiType
) {
}
