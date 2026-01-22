package econovation.moongtaengi.study.application;

import econovation.moongtaengi.collection.domain.CollectionType;
import econovation.moongtaengi.study.application.comment.CommentSummary;
import econovation.moongtaengi.study.domain.comment.CommentSummaryRaw;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentSummaryTest {

    @Test
    @DisplayName("로그인한 사용자가 작성자인 경우 isMyComment는 true여야 한다.")
    void 본인_댓글_판별_성공() {
        //given
        Long loginMemberId = 1L;
        Long writerId = 1L;

        CommentSummaryRaw raw = createRaw(writerId, CollectionType.KANE);

        //when
        CommentSummary summary = CommentSummary.of(loginMemberId, raw);

        //then
        assertThat(summary.isMyComment()).isTrue();
        assertThat(summary.memberId()).isEqualTo(writerId);
        assertThat(summary.profileImageUrl()).isEqualTo(CollectionType.KANE.getUnlockedImageUrl());
    }

    @Test
    @DisplayName("로그인한 사용자와 작성자가 다른 경우 isMyComment는 false여야 한다.")
    void 타인_댓글_판별_성공() {
        //given
        Long loginMemberId = 1L;
        Long writerId = 2L;

        CommentSummaryRaw raw = createRaw(writerId, CollectionType.KANE);

        // when
        CommentSummary summary = CommentSummary.of(loginMemberId, raw);

        // then
        assertThat(summary.isMyComment()).isFalse();
    }


    @Test
    @DisplayName("프로필 아이콘 정보가 없으면(null) 기본 이미지 URL을 반환해야 한다.")
    void 프로필_이미지_null일시_기본_URL_반환_성공() {
        // given
        Long loginMemberId = 1L;

        CommentSummaryRaw raw = createRaw(1L, null);

        // when
        CommentSummary summary = CommentSummary.of(loginMemberId, raw);

        // then
        assertThat(summary.profileImageUrl()).isEqualTo(CollectionType.DEFAULT.getUnlockedImageUrl());
    }

    private CommentSummaryRaw createRaw(Long writerId, CollectionType type) {
        return new CommentSummaryRaw(
                10L,
                "테스트 내용",
                LocalDateTime.now(),
                writerId,
                "닉네임",
                type
        );
    }
}