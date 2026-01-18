package econovation.moongtaengi.study.domain.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.Nickname;
import econovation.moongtaengi.study.application.assignment.AssignmentSummary;
import econovation.moongtaengi.study.domain.Study;
import econovation.moongtaengi.study.domain.StudyFixture;
import econovation.moongtaengi.study.domain.submission.SubmissionAttachment;
import econovation.moongtaengi.study.domain.submission.SubmissionFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class AssignmentRepositoryTest {
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private TestEntityManager em;


    @Test
    @DisplayName("프로세스 ID와 과제 수행자 ID로 과제 존재 여부를 확인한다")
    void 중복_과제_확인_성공() {
        //given
        Long processId = 100L;
        Long assigneeId = 1L;
        Long noAssigneeId = 999L;

        Assignment assignment = AssignmentFixture.anAssignment()
                .processId(processId)
                .assigneeId(assigneeId)
                .build();

        assignmentRepository.save(assignment);

        //when
        boolean exists = assignmentRepository.existsByProcessIdAndAssigneeId(processId, assigneeId);
        boolean notExists = assignmentRepository.existsByProcessIdAndAssigneeId(processId, noAssigneeId);

        //then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("스터디 멤버 기준으로 과제 목록 조회 (다른 스터디 멤버 제외)")
    void 과제_목록_조회_성공() {
        //given
        Long processId = 100L;

        Member host = createMember("방장");
        Member guest = createMember("게스트");

        Study myStudy = em.persistAndFlush(StudyFixture.aStudy(host.getId()));
        myStudy.addGuest(guest.getId());
        em.flush();

        Assignment assignment1 = em.persistAndFlush(AssignmentFixture.anAssignment()
                .processId(processId)
                .assigneeId(host.getId())
                .build());

        em.persistAndFlush(SubmissionFixture.aSubmission()
                .assignmentId(assignment1.getId())
                .submitterId(host.getId())
                .attachments(List.of(new SubmissionAttachment("http://url.com")))
                .build());

        assignment1.markAsSubmitted(false);
        em.flush();
        em.clear();

        //when
        List<AssignmentSummary> result =
                assignmentRepository.findSummaryByProcessIdAndStudyId(processId, myStudy.getId());

        //then
        assertThat(result).hasSize(2);

        AssignmentSummary dto1 = result.stream()
                .filter(d -> d.nickname().equals("방장"))
                .findFirst().get();

        assertThat(dto1.status()).isEqualTo(AssignmentStatus.SUBMITTED);
        assertThat(dto1.submissionId()).isNotNull();
        assertThat(dto1.memberId()).isEqualTo(host.getId());
        assertThat(dto1.fileUrl()).isEqualTo("http://url.com");

        AssignmentSummary dto2 = result.stream()
                .filter(d -> d.nickname().equals("게스트"))
                .findFirst().get();

        assertThat(dto2.assignmentId()).isNull();
    }


    private Member createMember(String nickname) {
        Member member = Member.createMember("kakao_" + nickname, new Nickname(nickname));
        return em.persistAndFlush(member);
    }

}
