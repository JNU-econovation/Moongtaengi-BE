package econovation.moongtaengi.study.domain.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.Nickname;
import econovation.moongtaengi.study.application.assignment.AssignmentSummary;
import econovation.moongtaengi.study.domain.Study;
import econovation.moongtaengi.study.domain.StudyFixture;
import econovation.moongtaengi.study.domain.StudyProcessFixture;
import econovation.moongtaengi.study.domain.process.StudyProcess;
import econovation.moongtaengi.study.domain.submission.Submission;
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
                .attachment(SubmissionAttachment.of("파일명.pdf", "http://url.com"))
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
        assertThat(dto1.fileName()).isEqualTo("파일명.pdf");
        assertThat(dto1.fileUrl()).isEqualTo("http://url.com");

        AssignmentSummary dto2 = result.stream()
                .filter(d -> d.nickname().equals("게스트"))
                .findFirst().get();

        assertThat(dto2.assignmentId()).isNull();
    }

    @Test
    @DisplayName("과제 상세 정보 조회 시 Member의 경험치와 Submission 정보(LEFT JOIN)가 포함되어야 한다")
    void 과제_상세_조회_제출물_정보_성공() {
        //given
        Member member = createMember("지환");
        member.addExperience(500);
        em.persistAndFlush(member);

        Study study = StudyFixture.aStudy(member.getId());
        em.persistAndFlush(study);

        StudyProcess process = StudyProcessFixture.aStudyProcess(study.getId());
        em.persistAndFlush(process);

        Assignment assignment = AssignmentFixture.anAssignment()
                .processId(process.getId())
                .assigneeId(member.getId())
                .build();
        em.persistAndFlush(assignment);

        Submission submission = SubmissionFixture.aSubmission()
                .assignmentId(assignment.getId())
                .submitterId(member.getId())
                .build();
        em.persistAndFlush(submission);

        //when
        AssignmentDetailRaw result = assignmentRepository.findDetailRawById(assignment.getId())
                .orElseThrow();

        //then
        assertThat(result.studyName()).isEqualTo(study.getName().getValue());
        assertThat(result.assignmentContent()).isEqualTo("테스트 과제");
        assertThat(result.nickname()).isEqualTo("지환");
        assertThat(result.totalExperience()).isEqualTo(500);
        assertThat(result.submissionId()).isEqualTo(submission.getId());
        assertThat(result.assigneeId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("제출하지 않은 과제를 조회하면 submission 관련 필드는 null이어야 한다")
    void 과제_상세_조회_제출물_없음_성공() {
        Member member = createMember("철수");
        em.persistAndFlush(member);

        Study study = StudyFixture.aStudy(member.getId());
        em.persistAndFlush(study);

        StudyProcess process = StudyProcessFixture.aStudyProcess(study.getId());
        em.persistAndFlush(process);

        Assignment assignment = AssignmentFixture.anAssignment()
                .processId(process.getId())
                .build();
        em.persistAndFlush(assignment);

        //when
        AssignmentDetailRaw result = assignmentRepository.findDetailRawById(assignment.getId())
                .orElseThrow();

        //then
        assertThat(result.submissionId()).isNull();
        assertThat(result.submitTime()).isNull();
        assertThat(result.assignmentContent()).isEqualTo("테스트 과제");
    }


    private Member createMember(String nickname) {
        Member member = Member.createMember("kakao_" + nickname, new Nickname(nickname));
        return em.persistAndFlush(member);
    }
}
