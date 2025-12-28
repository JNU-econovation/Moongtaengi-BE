package econovation.moongtaengi.study.infra;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.MemberRepository;
import econovation.moongtaengi.study.domain.StudyErrorCode;
import econovation.moongtaengi.study.domain.StudyException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StudyCreationValidatorImplTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private StudyCreationValidatorImpl studyCreationValidator;

    @Test
    @DisplayName("정회원이 아닌 임시 회원이면 예외가 발생한다.")
    void 정회원_검증_실패() {
        //given
        Long memberId = 1L;

        Member tempMember = mock(Member.class);
        given(tempMember.isTemporary()).willReturn(true);

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(tempMember));

        //when&then
        assertThatThrownBy(() -> studyCreationValidator.validate(memberId))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.STUDY_CREATION_DENIED_TEMP_MEMBER);
    }

    @Test
    @DisplayName("정회원일 경우 검증을 통과한다.")
    void 정회원_검증_성공() {
        //given
        Long memberId = 1L;

        Member activeMember = mock(Member.class);
        given(activeMember.isTemporary()).willReturn(false);

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(activeMember));

        //when&then
        assertDoesNotThrow(() -> studyCreationValidator.validate(memberId));
    }

    @Test
    @DisplayName("존재하지 않는 회원일 경우 예외가 발생한다.")
    void 존재하지않는_회원() {
        //given
        Long memberId = 1L;
        given(memberRepository.findById(memberId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> studyCreationValidator.validate(memberId))
                .isInstanceOf(StudyException.class);
    }

}
