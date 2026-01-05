package econovation.moongtaengi.study.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import econovation.moongtaengi.study.domain.InviteCode;
import econovation.moongtaengi.study.domain.Study;
import econovation.moongtaengi.study.domain.StudyErrorCode;
import econovation.moongtaengi.study.domain.StudyException;
import econovation.moongtaengi.study.domain.StudyJoinValidator;
import econovation.moongtaengi.study.domain.StudyName;
import econovation.moongtaengi.study.domain.StudyPeriod;
import econovation.moongtaengi.study.domain.StudyRepository;
import econovation.moongtaengi.study.domain.StudyTopic;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JoinStudyServiceTest {
    @InjectMocks
    private JoinStudyService joinStudyService;

    @Mock
    private StudyRepository studyRepository;

    @Mock
    private StudyJoinValidator studyJoinValidator;

    @Test
    @DisplayName("유효한 초대코드로 스터디 가입에 성공한다.")
    void 스터디_가입_성공() {
        //given
        Long memberId = 100L;
        String code = "12345678";
        InviteCode inviteCode = new InviteCode(code);

        Study study = createStudyWithInviteCode(inviteCode);
        int initialMemberCount = study.getMembers().size();

        given(studyRepository.findByInviteCode(inviteCode))
                .willReturn(Optional.of(study));
        willDoNothing().given(studyJoinValidator).validate(memberId, study);

        //when
        joinStudyService.joinStudy(memberId, code);

        //then
        assertAll(
                () -> assertThat(study.getMembers()).hasSize(initialMemberCount + 1),
                () -> assertThat(study.getMembers())
                        .extracting("memberId")
                        .contains(memberId)
        );
    }

    @Test
    @DisplayName("존재하지 않는 초대코드로 가입 시도 시 예외가 발생한다.")
    void 존재하지_않는_초대코드_실패() {
        // given
        Long memberId = 100L;
        String code = "99999999";
        InviteCode inviteCode = new InviteCode(code);

        given(studyRepository.findByInviteCode(inviteCode))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> joinStudyService.joinStudy(memberId, code))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.STUDY_NOT_FOUND);
    }

    @Test
    @DisplayName("검증기(임시회원 등)를 통과하지 못하면 가입할 수 없다.")
    void 가입_검증_실패() {
        // given
        Long memberId = 100L;
        String code = "12345678";
        InviteCode inviteCode = new InviteCode(code);

        Study study = createStudyWithInviteCode(inviteCode);

        given(studyRepository.findByInviteCode(inviteCode))
                .willReturn(Optional.of(study));

        willThrow(new StudyException(StudyErrorCode.STUDY_JOIN_DENIED_TEMP_MEMBER))
                .given(studyJoinValidator).validate(memberId, study);

        // when & then
        assertThatThrownBy(() -> joinStudyService.joinStudy(memberId, code))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.STUDY_JOIN_DENIED_TEMP_MEMBER);
    }


    private Study createStudyWithInviteCode(InviteCode inviteCode) {
        return new Study(
                new StudyName("테스트 스터디"),
                new StudyPeriod(LocalDate.now(), LocalDate.now().plusDays(7)),
                new StudyTopic("테스트 주제"),
                1L, //임의의 방장 ID
                inviteCode
        );
    }
}
