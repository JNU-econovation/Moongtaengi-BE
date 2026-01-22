package econovation.moongtaengi.study.application.comment;

import econovation.moongtaengi.collection.domain.CollectionType;
import econovation.moongtaengi.study.domain.comment.CommentSummaryRaw;
import java.time.LocalDateTime;

public record CommentSummary(
        Long commentId,
        String content,
        LocalDateTime createdAt,
        Long memberId,
        String nickname,
        String profileImageUrl,
        boolean isMyComment
) {
    public static CommentSummary of(Long loginMemberId, CommentSummaryRaw raw) {
        return new CommentSummary(
                raw.commentId(),
                raw.content(),
                raw.createdAt(),
                raw.memberId(),
                raw.nickname(),
                getProfileIconUrl(raw.profileIcon()),
                isMyComment(loginMemberId, raw.memberId())
        );
    }

    private static String getProfileIconUrl(CollectionType type) {
        if (type == null) {
            return CollectionType.DEFAULT.getUnlockedImageUrl();
        }
        return type.getUnlockedImageUrl();
    }

    private static boolean isMyComment(Long loginMemberId, Long writerId) {
        return loginMemberId != null && loginMemberId.equals(writerId);
    }
}
