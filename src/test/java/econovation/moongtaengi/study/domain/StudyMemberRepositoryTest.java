package econovation.moongtaengi.study.domain;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.PersistenceUnitUtil;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class StudyMemberRepositoryTest {
    @Autowired
    private StudyMemberRepository studyMemberRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("회원 ID와 역할로 조회 시, 스터디 엔티티까지 fetch join으로 한 번에 가져온다.")
    void 회원_ID_역할_조회_성공() {
        //given
        Long memberId = 1L;

        Study study = new Study(
                new StudyName("테스트 스터디"),
                new StudyPeriod(LocalDate.now(), LocalDate.now().plusDays(7)),
                new StudyTopic("테스트 주제"),
                memberId,
                new InviteCode("12345678")
        );
        entityManager.persist(study);
        entityManager.flush();
        entityManager.clear();

        //when
        List<StudyMember> result = studyMemberRepository.findAllByMemberIdAndRole(memberId, StudyRole.HOST);

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getStudy().getName().getValue()).isEqualTo("테스트 스터디");

        PersistenceUnitUtil util = entityManager.getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
        boolean isLoaded = util.isLoaded(result.getFirst().getStudy());

        assertThat(isLoaded).isTrue();
    }
}
