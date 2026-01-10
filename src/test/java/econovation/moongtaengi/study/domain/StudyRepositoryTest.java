package econovation.moongtaengi.study.domain;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class StudyRepositoryTest {
    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("초대 코드로 스터디를 조회할 수 있다.")
    void 초대코드_스터디_조회_성공() {
        //given
        InviteCode inviteCode = new InviteCode("12345678");
        Study study = createStudyWithInviteCode(inviteCode);
        studyRepository.save(study);

        //when
        Optional<Study> result = studyRepository.findByInviteCode(inviteCode);

        //then
        assertThat(result).isPresent();
        assertThat(result.get().getInviteCode()).isEqualTo(inviteCode);
    }

    @Test
    @DisplayName("존재하지 않는 초대 코드로 조회하면 빈 Optional이 반환된다.")
    void 초대코드_스터디_조회_실패() {
        //given
        InviteCode wrongInviteCode = new InviteCode("99999999");

        //when
        Optional<Study> result = studyRepository.findByInviteCode(wrongInviteCode);

        //then
        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("ID로 스터디 조회 시 멤버들도 Fetch Join으로 함께 조회한다.")
    void 스터디_멤버_페치조인_조회() {
        //given
        InviteCode inviteCode = new InviteCode("12345678");
        Study study = createStudyWithInviteCode(inviteCode);
        studyRepository.save(study);

        study.addGuest(2L);
        studyRepository.save(study);

        entityManager.flush();
        entityManager.clear();

        //when
        Optional<Study> result = studyRepository.findByIdWithMembers(study.getId());

        //then
        assertThat(result).isPresent();
        assertThat(result.get().getMembers()).hasSize(2);
        assertThat(result.get().getMembers())
                .extracting("memberId")
                .contains(1L, 2L);
    }

    private Study createStudyWithInviteCode(InviteCode inviteCode) {
        return new Study(
                new StudyName("테스트 스터디"),
                new StudyPeriod(LocalDate.now(), LocalDate.now().plusDays(7)),
                new StudyTopic("테스트 주제"),
                1L,
                inviteCode
        );
    }
}
