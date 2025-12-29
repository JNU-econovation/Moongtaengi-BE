package econovation.moongtaengi.study.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StudyTest {
    @Test
    @DisplayName("스터디 생성 시 이름이 올바르게 설정되고, 생성자가 방장으로 등록된다.")
    void 스터디_생성_성공() {
        // given
        StudyName name = new StudyName("스프링 스터디");
        StudyPeriod period = new StudyPeriod(LocalDate.now(), LocalDate.now().plusDays(7));
        StudyTopic topic = new StudyTopic("스프링부트 JPA");
        Long hostId = 1L;
        InviteCode inviteCode = new InviteCode("12345678");

        // when
        Study study = new Study(name, period, topic, hostId, inviteCode);

        // then
        assertThat(study).isNotNull();
        assertThat(study.getName()).isEqualTo(name);
        assertThat(study.getMembers()).hasSize(1);
        assertThat(study.getMembers().get(0).getMemberId()).isEqualTo(hostId);
    }

    @Test
    @DisplayName("새로운 회원은 스터디에 정상적으로 가입할 수 있다.")
    void 스터디_가입_성공() {
        //given
        Long hostId = 1L;
        Study study = createStudy(hostId);
        Long newMemberId = 2L;

        //when
        study.addMember(newMemberId, StudyRole.GUEST);

        //then
        assertThat(study.getMembers()).hasSize(2);
        assertThat(study.getMembers())
                .extracting("memberId")
                .contains(1L, 2L);
    }

    @Test
    @DisplayName("이미 참여 중인 회원이 가입을 시도하면 예외가 발생한다.")
    void 스터디_가입_중복_실패() {
        //given
        Long hostId = 1L;
        Study study = createStudy(hostId);

        //when&then
        assertThatThrownBy(() -> study.addMember(hostId, StudyRole.GUEST))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.ALREADY_JOINED_MEMBER);
    }

    private Study createStudy(Long hostId) {
        return new Study(
                new StudyName("테스트 스터디"),
                new StudyPeriod(LocalDate.now(), LocalDate.now().plusDays(7)),
                new StudyTopic("테스트 주제"),
                hostId,
                new InviteCode("12345678")
        );
    }
}

