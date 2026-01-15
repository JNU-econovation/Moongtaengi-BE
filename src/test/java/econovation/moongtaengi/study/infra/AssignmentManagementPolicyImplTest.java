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
    @DisplayName("권한이 없는 멤버가 요청하면 예외가 발생한다")
    void 권한_검증_실패() {
        //given
        Long studyId = 1L;
        Long requesterId = 99L;

        given(studyMemberRepository.existsByStudyIdAndMemberIdAndRole(
                studyId, requesterId, StudyRole.HOST))
                .willReturn(false);

        //when&then
        assertThatThrownBy(() -> policy.validate(studyId, requesterId))
                .isInstanceOf(AssignmentException.class)
                .extracting("errorCode")
                .isEqualTo(AssignmentErrorCode.NO_MANAGEMENT_PERMISSION);
    }

    @Test
    @DisplayName("권한이 있는 멤버가 요청하면 통과한다")
    void 권한_검증_성공() {
        //given
        Long studyId = 1L;
        Long requesterId = 10L;

        given(studyMemberRepository.existsByStudyIdAndMemberIdAndRole(
                studyId, requesterId, StudyRole.HOST))
                .willReturn(true);

        //when&then
        assertDoesNotThrow(() -> policy.validate(studyId, requesterId));
    }
}
