package econovation.moongtaengi.study.domain.reaction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmojiType {
    HEART("하트"),
    CLAP("박수"),
    SURPRISED("놀람"),
    SAD("슬픔"),
    EYES_HEART("눈하트");

    private final String description;
}
