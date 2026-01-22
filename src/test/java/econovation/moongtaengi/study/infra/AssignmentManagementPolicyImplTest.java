package econovation.moongtaengi.study.infra;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

import econovation.moongtaengi.study.domain.StudyMemberRepository;
import econovation.moongtaengi.study.domain.StudyRole;
import econovation.moongtaengi.study.domain.assignment.AssignmentErrorCode;
import econovation.moongtaengi.study.domain.assignment.AssignmentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AssignmentManagementPolicyImplTest {
    @Mock
    private StudyMemberRepository studyMemberRepository;

    @InjectMocks
    private AssignmentManagementPolicyImpl policy;

    @Test
    @DisplayName("관리 권한 검증(validateAuthority)을 할 때 방장이 아니면 예외가 발생한다")
    void 권한_검증_실패() {
        //given
        Long studyId = 1L;
        Long requesterId = 99L;

        given(studyMemberRepository.existsByStudyIdAndMemberIdAndRole(
                studyId, requesterId, StudyRole.HOST))
                .willReturn(false);

        //when&then
        assertThatThrownBy(() -> policy.validateAuthority(studyId, requesterId))
                .isInstanceOf(AssignmentException.class)
                .extracting("errorCode")
                .isEqualTo(AssignmentErrorCode.NO_MANAGEMENT_PERMISSION);
    }

    @Test
    @DisplayName("할당 검증(validateAllocation)을 할 때 방장 권한이 없으면, 멤버 검증 로직은 실행되지 않고 바로 예외가 터진다")
    void 할당_검증_실패_권한없음() {
        //given
        Long studyId = 1L;
        Long requesterId = 99L;
        Long assigneeId = 3L;

        given(studyMemberRepository.existsByStudyIdAndMemberIdAndRole(
                studyId, requesterId, StudyRole.HOST))
                .willReturn(false);

        //when&then
        assertThatThrownBy(() -> policy.validateAllocation(studyId, requesterId, assigneeId))
                .isInstanceOf(AssignmentException.class)
                .extracting("errorCode")
                .isEqualTo(AssignmentErrorCode.NO_MANAGEMENT_PERMISSION);
    }

    @Test
    @DisplayName("할당 검증을 할 때 방장은 맞지만, 대상자가 스터디원이 아니면 예외가 발생한다")
    void 할당_검증_실패_대상자아님() {
        //given
        Long studyId = 1L;
        Long requesterId = 10L;
        Long assigneeId = 999L;

        given(studyMemberRepository.existsByStudyIdAndMemberIdAndRole(
                studyId, requesterId, StudyRole.HOST))
                .willReturn(true);
        given(studyMemberRepository.existsByStudyIdAndMemberId(
                studyId, assigneeId))
                .willReturn(false);

        //when&then
        assertThatThrownBy(() -> policy.validateAllocation(studyId, requesterId, assigneeId))
                .isInstanceOf(AssignmentException.class)
                .extracting("errorCode")
                .isEqualTo(AssignmentErrorCode.INVALID_ASSIGNEE);
    }

    @Test
    @DisplayName("할당 검증을 할 때 방장이고 대상자도 멤버라면 성공한다")
    void 할당_검증_성공() {
        //given
        Long studyId = 1L;
        Long requesterId = 10L;
        Long assigneeId = 11L;

        given(studyMemberRepository.existsByStudyIdAndMemberIdAndRole(
                studyId, requesterId, StudyRole.HOST))
                .willReturn(true);
        given(studyMemberRepository.existsByStudyIdAndMemberId(
                studyId, assigneeId))
                .willReturn(true);

        //when&then
        assertDoesNotThrow(() -> policy.validateAllocation(studyId, requesterId, assigneeId));
    }
}
