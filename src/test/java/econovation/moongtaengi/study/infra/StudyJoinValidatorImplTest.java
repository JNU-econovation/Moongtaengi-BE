package econovation.moongtaengi.study.infra;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.MemberRepository;
import econovation.moongtaengi.study.domain.Study;
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
public class StudyJoinValidatorImplTest {
    @InjectMocks
    private StudyJoinValidatorImpl studyJoinValidator;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private Study study;

    @Test
    @DisplayName("임시회원은 스터디에 가입할 수 없다.")
    void 임시회원_가입_실패() {
        //given
        Long tempMemberId = 1L;
        Member tempMember = mock(Member.class);
        given(tempMember.isTemporary())
                .willReturn(true);

        given(memberRepository.findById(tempMemberId))
                .willReturn(Optional.of(tempMember));

        //when&then
        assertThatThrownBy(() -> studyJoinValidator.validate(tempMemberId, study))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.STUDY_JOIN_DENIED_TEMP_MEMBER);
    }

    @Test
    @DisplayName("정회원은 정상적으로 스터디 참여가 가능하다.")
    void 정회원_가입_성공() {
        //given
        Long memberId = 1L;
        Member member = mock(Member.class);

        given(member.isTemporary())
                .willReturn(false);
        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));

        //when&then
        assertDoesNotThrow(() -> studyJoinValidator.validate(memberId, study));
    }

    @Test
    @DisplayName("존재하지 않는 회원일 경우 JOINER_NOT_FOUND 예외가 발생한다.")
    void 존재하지_않는_회원_실패() {
        //given
        Long memberId = 999L;

        given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());

        //when&then
        assertThatThrownBy(() -> studyJoinValidator.validate(memberId, study))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.JOINER_NOT_FOUND);

    }
}
