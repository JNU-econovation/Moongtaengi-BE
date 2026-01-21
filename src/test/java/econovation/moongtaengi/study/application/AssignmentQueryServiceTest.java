package econovation.moongtaengi.study.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import econovation.moongtaengi.member.domain.Title;
import econovation.moongtaengi.study.application.assignment.AssignmentDetail;
import econovation.moongtaengi.study.application.assignment.AssignmentQueryService;
import econovation.moongtaengi.study.application.assignment.AssignmentSummary;
import econovation.moongtaengi.study.domain.StudyErrorCode;
import econovation.moongtaengi.study.domain.StudyException;
import econovation.moongtaengi.study.domain.StudyMemberRepository;
import econovation.moongtaengi.study.domain.assignment.AssignmentDetailRaw;
import econovation.moongtaengi.study.domain.assignment.AssignmentErrorCode;
import econovation.moongtaengi.study.domain.assignment.AssignmentException;
import econovation.moongtaengi.study.domain.assignment.AssignmentRepository;
import econovation.moongtaengi.study.domain.process.StudyProcessRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssignmentQueryServiceTest {

    @InjectMocks
    AssignmentQueryService assignmentQueryService;

    @Mock
    AssignmentRepository assignmentRepository;
    @Mock
    StudyProcessRepository processRepository;
    @Mock
    StudyMemberRepository studyMemberRepository;

    @Test
    @DisplayName("스터디 멤버가 존재하는 프로세스의 과제 목록을 조회하면, 과제 리스트를 반환한다.")
    void 과제_목록_조회_성공() {
        // given
        Long memberId = 1L;
        Long processId = 100L;
        Long studyId = 10L;

        given(processRepository.findStudyIdById(processId))
                .willReturn(Optional.of(studyId));

        given(studyMemberRepository.existsByStudyIdAndMemberId(studyId, memberId))
                .willReturn(true);

        List<AssignmentSummary> expectedResponse = List.of();
        given(assignmentRepository.findSummaryByProcessIdAndStudyId(processId, studyId))
                .willReturn(expectedResponse);

        //when
        List<AssignmentSummary> result = assignmentQueryService.getAssignmentSummaries(memberId,
                processId);

        //then
        assertThat(result).isEqualTo(expectedResponse);
        verify(assignmentRepository).findSummaryByProcessIdAndStudyId(processId, studyId);
    }

    @Test
    @DisplayName("존재하지 않는 프로세스 ID로 조회하면, PROCESS_NOT_FOUND 예외가 발생한다.")
    void 과제_목록_조회_존재하지_않는_프로세스_실패() {
        // given
        given(processRepository.findStudyIdById(anyLong())).willReturn(Optional.empty());

        //when&then
        assertThatThrownBy(() ->
                assignmentQueryService.getAssignmentSummaries(1L, 999L))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.PROCESS_NOT_FOUND);
    }

    @Test
    @DisplayName("해당 스터디에 참여하지 않은 멤버가 조회하면, NOT_STUDY_MEMBER 예외가 발생한다.")
    void 과제_목록_조회_스터디_멤버가_아님_실패() {
        // given
        Long memberId = 1L;
        Long processId = 100L;
        Long studyId = 10L;

        given(processRepository.findStudyIdById(processId)).willReturn(Optional.of(studyId));

        given(studyMemberRepository.existsByStudyIdAndMemberId(studyId, memberId))
                .willReturn(false);

        //when&then
        assertThatThrownBy(() ->
                assignmentQueryService.getAssignmentSummaries(memberId, processId))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.NOT_STUDY_MEMBER);
    }

    @Test
    @DisplayName("과제 상세 조회 성공 및 칭호 변환과 본인 여부가 정확해야 한다")
    void 과제_상세_조회_성공() {
        //given
        Long loginMemberId = 1L;
        Long assignmentId = 10L;
        Long assigneeId = 1L;
        Long studyId = 100L;
        int experience = 100;

        AssignmentDetailRaw raw = new AssignmentDetailRaw(
                studyId,
                "테스트 스터디",
                "테스트 과제",
                "지환",
                assigneeId,
                experience,
                null,
                null
        );

        given(assignmentRepository.findDetailRawById(assignmentId))
                .willReturn(Optional.of(raw));

        given(studyMemberRepository.existsByStudyIdAndMemberId(studyId, loginMemberId))
                .willReturn(true);

        // when
        AssignmentDetail result = assignmentQueryService.getAssignmentDetail(loginMemberId, assignmentId);

        // then
        assertThat(result.memberTitle()).isEqualTo(Title.fromExperience(experience).getDisplayName());
        assertThat(result.isOwner()).isTrue();
        assertThat(result.studyName()).isEqualTo("테스트 스터디");
        assertThat(result.assignmentDescription()).isEqualTo("테스트 과제");
    }

    @Test
    @DisplayName("타인의 과제를 조회하면 isOwner는 false여야 한다")
    void 타인의_과제는_isOwner_false() {
        //given
        Long loginMemberId = 999L;
        Long assignmentId = 10L;
        Long assigneeId = 1L;
        Long studyId = 100L;

        AssignmentDetailRaw raw = new AssignmentDetailRaw(
                studyId,
                "스터디",
                "내용",
                "닉네임",
                assigneeId,
                0,
                null,
                null
        );

        given(assignmentRepository.findDetailRawById(assignmentId))
                .willReturn(Optional.of(raw));

        given(studyMemberRepository.existsByStudyIdAndMemberId(studyId, loginMemberId))
                .willReturn(true);

        //when
        AssignmentDetail result = assignmentQueryService.getAssignmentDetail(loginMemberId, assignmentId);

        //then
        assertThat(result.isOwner()).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 과제 조회 시 예외가 발생해야 한다")
    void 존재하지_않는_과제_조회_실패() {
        // given
        given(assignmentRepository.findDetailRawById(any()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                assignmentQueryService.getAssignmentDetail(1L, 999L)
        )
                .isInstanceOf(AssignmentException.class)
                .extracting("errorCode")
                .isEqualTo(AssignmentErrorCode.ASSIGNMENT_NOT_FOUND);
    }

    @Test
    @DisplayName("스터디의 멤버가 아닐 시 과제 조회를 할 경우 예외가 발생한다.")
    void 스터디_멤버_아닐_시_단건_조회_실패() {
        //given
        Long studyId = 1L;
        Long loginMemberId = 999L;
        Long assignmentId = 10L;
        Long assigneeId = 1L;

        AssignmentDetailRaw raw = new AssignmentDetailRaw(
                studyId,
                "테스트 스터디",
                "테스트 내용",
                "지환",
                assigneeId,
                0,
                null,
                null
        );

        given(assignmentRepository.findDetailRawById(assignmentId))
                .willReturn(Optional.of(raw));
        given(studyMemberRepository.existsByStudyIdAndMemberId(studyId, loginMemberId))
                .willReturn(false);

        //when&then
        assertThatThrownBy(() ->
                assignmentQueryService.getAssignmentDetail(loginMemberId, assignmentId)
        )
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.NOT_STUDY_MEMBER);


    }

}
