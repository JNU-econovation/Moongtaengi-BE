package econovation.moongtaengi.study.infra;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

import econovation.moongtaengi.study.domain.StudyMemberRepository;
import econovation.moongtaengi.study.domain.comment.CommentErrorCode;
import econovation.moongtaengi.study.domain.comment.CommentException;
import econovation.moongtaengi.study.domain.submission.SubmissionRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentMemberValidatorImplTest {
    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private StudyMemberRepository studyMemberRepository;

    @InjectMocks
    private CommentMemberValidatorImpl validator;

    @Test
    @DisplayName("스터디원이고 제출물이 존재하면 통과한다.")
    void 스터디원_제출물_존재_검증_성공() {
        //given
        Long memberId = 1L;
        Long submissionId = 100L;
        Long studyId = 777L;

        given(submissionRepository.findStudyIdById(submissionId))
                .willReturn(Optional.of(studyId));

        given(studyMemberRepository.existsByStudyIdAndMemberId(studyId, memberId))
                .willReturn(true);

        //when&then
        assertDoesNotThrow(() -> validator.validate(memberId, submissionId));
    }

    @Test
    @DisplayName("제출물이 존재하지 않으면 예외가 발생한다.")
    void 제출물_없음_검증_실패() {
        //given
        Long memberId = 1L;
        Long submissionId = 999L;

        given(submissionRepository.findStudyIdById(submissionId))
                .willReturn(Optional.empty());

        //when&then
        assertThatThrownBy(() -> validator.validate(memberId, submissionId))
                .isInstanceOf(CommentException.class)
                .extracting("errorCode")
                .isEqualTo(CommentErrorCode.COMMENT_NOT_FOUND);
    }

    @Test
    @DisplayName("스터디원이 아니면 예외가 발생한다.")
    void 스터디원_아님_검증_실패() {
        // given
        Long memberId = 2L;
        Long submissionId = 100L;
        Long studyId = 777L;

        given(submissionRepository.findStudyIdById(submissionId))
                .willReturn(Optional.of(studyId));

        given(studyMemberRepository.existsByStudyIdAndMemberId(studyId, memberId))
                .willReturn(false);

        //when&then
        assertThatThrownBy(() -> validator.validate(memberId, submissionId))
                .isInstanceOf(CommentException.class)
                .extracting("errorCode")
                .isEqualTo(CommentErrorCode.NO_PERMISSION);
    }
}
