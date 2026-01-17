package econovation.moongtaengi.study.domain.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.Nickname;
import econovation.moongtaengi.study.application.assignment.AssignmentSummary;
import econovation.moongtaengi.study.domain.InviteCode;
import econovation.moongtaengi.study.domain.Study;
import econovation.moongtaengi.study.domain.StudyName;
import econovation.moongtaengi.study.domain.StudyPeriod;
import econovation.moongtaengi.study.domain.StudyTopic;
import econovation.moongtaengi.study.domain.submission.Submission;
import econovation.moongtaengi.study.domain.submission.SubmissionAttachment;
import econovation.moongtaengi.study.domain.submission.SubmissionContent;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

        Study myStudy = createStudy(host.getId());

        myStudy.addGuest(guest.getId());
        em.persistAndFlush(myStudy);

        Assignment assignment1 = createAssignment(processId, host.getId());

        createSubmission(assignment1, host);

        assignment1.markAsSubmitted(false);
        em.persistAndFlush(assignment1);

        em.clear();

        //when
        List<AssignmentSummary> result =
                assignmentRepository.findSummaryByProcessIdAndStudyId(processId, myStudy.getId());

        //then
        assertThat(result).hasSize(2);

        AssignmentSummary dto1 = result.stream()
                .filter(d -> d.nickname().equals("방장")) // ✨ 닉네임 일치!
                .findFirst().get();
        assertThat(dto1.status()).isEqualTo(AssignmentStatus.SUBMITTED);

        AssignmentSummary dto2 = result.stream()
                .filter(d -> d.nickname().equals("게스트")) // ✨ 닉네임 일치!
                .findFirst().get();
        assertThat(dto2.assignmentId()).isNull();
    }


    private Member createMember(String nickname) {
        Member member = Member.createMember("kakao_" + nickname, new Nickname(nickname));
        return em.persistAndFlush(member);
    }

    private Study createStudy(Long hostId) {
        Study study = new Study(
                new StudyName("테스트 스터디"),
                new StudyPeriod(LocalDate.now(), LocalDate.now().plusDays(30)),
                new StudyTopic("테스트 주제"),
                hostId,
                new InviteCode("12345678")
        );
        return em.persistAndFlush(study);
    }

    private Assignment createAssignment(Long processId, Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        Assignment assignment = Assignment.create(
                processId,
                memberId,
                new AssignmentContent("과제"),
                AssignmentDeadline.create(now, now.toLocalDate(), now.plusDays(1).toLocalDate())
        );
        return em.persistAndFlush(assignment);
    }

    private void createSubmission(Assignment assignment, Member member) {
        Submission submission = Submission.create(
                assignment.getId(),
                member.getId(),
                new SubmissionContent("내용"),
                LocalDateTime.now(),
                assignment.getDeadline().getValue(),
                List.of(new SubmissionAttachment("http://url.com"))
        );
        em.persistAndFlush(submission);
    }

}
