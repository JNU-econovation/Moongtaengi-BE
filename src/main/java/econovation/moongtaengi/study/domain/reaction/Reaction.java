package econovation.moongtaengi.study.domain.reaction;

import econovation.moongtaengi.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "reactions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_reaction_submission_member_type",
                        columnNames = {"submission_id", "member_id", "emoji_type"}
                )
        }
)
public class Reaction extends BaseEntity {
    @Column(name = "submission_id", nullable = false)
    private Long submissionId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(name = "emoji_type", nullable = false)
    private EmojiType emojiType;

    private Reaction(Long submissionId, Long memberId, EmojiType emojiType) {
        this.submissionId = submissionId;
        this.memberId = memberId;
        this.emojiType = emojiType;
    }

    public static Reaction create(Long submissionId, Long memberId, EmojiType emojiType) {
        validate(submissionId, memberId, emojiType);
        return new Reaction(submissionId, memberId, emojiType);
    }

    private static void validate(Long submissionId, Long memberId, EmojiType emojiType) {
        if (submissionId == null || memberId == null || emojiType == null) {
            throw new ReactionException(ReactionErrorCode.INVALID_REACTION_INFO);
        }
    }
}
