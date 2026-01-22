package econovation.moongtaengi.study.domain.comment;

import econovation.moongtaengi.collection.domain.CollectionType;
import java.time.LocalDateTime;

public record CommentSummaryRaw(
        Long commentId,
        String content,
        LocalDateTime createdAt,
        Long memberId,
        String nickname,
        CollectionType profileIcon
) {

}
