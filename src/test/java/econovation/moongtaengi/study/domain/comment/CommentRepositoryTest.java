package econovation.moongtaengi.study.domain.comment;

import static org.assertj.core.api.Assertions.assertThat;

import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.Nickname;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EntityManager em;

    @Test
    void 제출물_댓글_목록_String_프로젝션_조회_성공() {
        //given
        Member member = Member.createMember("KAKAO_", new Nickname("지환"));
        em.persist(member);

        CommentContent contentVO = new CommentContent("댓글1");
        Comment comment = Comment.create(1L, member.getId(), contentVO);

        em.persist(comment);
        em.flush();
        em.clear();

        //when
        List<CommentSummaryRaw> result = commentRepository.findRawListBySubmissionId(1L);

        //then
        assertThat(result).hasSize(1);

        CommentSummaryRaw raw = result.get(0);

        assertThat(raw.content()).isEqualTo("댓글1");
        assertThat(raw.nickname()).isEqualTo("지환");
    }
}
