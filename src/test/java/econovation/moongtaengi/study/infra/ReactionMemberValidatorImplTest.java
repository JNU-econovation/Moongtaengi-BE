package econovation.moongtaengi.study.infra;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

import econovation.moongtaengi.study.domain.StudyMemberRepository;
import econovation.moongtaengi.study.domain.reaction.ReactionErrorCode;
import econovation.moongtaengi.study.domain.reaction.ReactionException;
import econovation.moongtaengi.study.domain.submission.SubmissionRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReactionMemberValidatorImplTest {
    @InjectMocks
    private ReactionMemberValidatorImpl validator;

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private StudyMemberRepository studyMemberRepository;

    @Test
    @DisplayName("스터디 멤버라면 제출물에 감정표현을 남길 수 있다.")
    void 스터디_멤버_감정표현_성공() {
        //given
        Long memberId = 1L;
        Long submissionId = 10L;
        Long studyId = 100L;

        given(submissionRepository.findStudyIdById(submissionId)).willReturn(Optional.of(studyId));
        given(studyMemberRepository.existsByStudyIdAndMemberId(studyId, memberId)).willReturn(true);

        //when&then
        assertDoesNotThrow(() -> validator.validate(memberId, submissionId));
    }

    @Test
    @DisplayName("존재하지 않는 제출물이라면 예외가 발생한다.")
    void validate_fail_submission_not_found() {
        //given
        Long memberId = 1L;
        Long submissionId = 999L;

        given(submissionRepository.findStudyIdById(submissionId)).willReturn(Optional.empty());

        //when&then
        assertThatThrownBy(() -> validator.validate(memberId, submissionId))
                .isInstanceOf(ReactionException.class)
                .extracting("errorCode")
                .isEqualTo(ReactionErrorCode.SUBMISSION_NOT_FOUND);
    }

    @Test
    @DisplayName("스터디 멤버가 아니라면 감정표현을 남길 권한이 없다.")
    void validate_fail_no_permission() {
        //given
        Long memberId = 999L;
        Long submissionId = 10L;
        Long studyId = 100L;

        given(submissionRepository.findStudyIdById(submissionId)).willReturn(Optional.of(studyId));
        given(studyMemberRepository.existsByStudyIdAndMemberId(studyId, memberId)).willReturn(false);

        //when&then
        assertThatThrownBy(() -> validator.validate(memberId, submissionId))
                .isInstanceOf(ReactionException.class)
                .extracting("errorCode")
                .isEqualTo(ReactionErrorCode.NO_PERMISSION);
    }

}
