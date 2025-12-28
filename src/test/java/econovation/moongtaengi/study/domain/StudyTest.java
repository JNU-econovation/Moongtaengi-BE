package econovation.moongtaengi.study.domain;

import static org.assertj.core.api.Assertions.assertThat;

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
}

