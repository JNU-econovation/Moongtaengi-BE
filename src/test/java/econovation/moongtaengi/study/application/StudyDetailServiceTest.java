package econovation.moongtaengi.study.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import econovation.moongtaengi.study.api.dto.StudyDetailResponse;
import econovation.moongtaengi.study.domain.InviteCode;
import econovation.moongtaengi.study.domain.Study;
import econovation.moongtaengi.study.domain.StudyErrorCode;
import econovation.moongtaengi.study.domain.StudyException;
import econovation.moongtaengi.study.domain.StudyMember;
import econovation.moongtaengi.study.domain.StudyMemberRepository;
import econovation.moongtaengi.study.domain.StudyName;
import econovation.moongtaengi.study.domain.StudyPeriod;
import econovation.moongtaengi.study.domain.StudyRole;
import econovation.moongtaengi.study.domain.StudyTopic;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class StudyDetailServiceTest {
    @InjectMocks
    private StudyDetailService studyDetailService;

    @Mock
    private StudyMemberRepository studyMemberRepository;

    @Test
    @DisplayName("studyId와 memberId를 통해 스터디 상세 조회에 성공한다.")
    void 스터디_상세_조회_성공() {
        //given
        Long memberId = 1L;
        Long studyId = 100L;
        LocalDate startDate = LocalDate.of(2026, 1, 8);
        LocalDate endDate = LocalDate.of(2026, 3, 20);
        Study study = new Study(
                new StudyName("테스트 스터디"),
                new StudyPeriod(startDate, endDate),
                new StudyTopic("테스트 주제"),
                memberId,
                new InviteCode("12345678")
        );
        ReflectionTestUtils.setField(study, "id", studyId);

        StudyMember studyMember = new StudyMember(study, memberId, StudyRole.HOST);
        given(studyMemberRepository.findByStudyIdAndMemberId(studyId, memberId))
                .willReturn(Optional.of(studyMember));

        //when
        StudyDetailResponse result = studyDetailService.getStudyDetail(studyId, memberId);

        //then
        assertThat(result.studyId()).isEqualTo(studyId);
        assertThat(result.studyName()).isEqualTo("테스트 스터디");

        assertThat(result.studyPeriod().startDate()).isEqualTo(startDate);
        assertThat(result.studyPeriod().endDate()).isEqualTo(endDate);

        assertThat(result.myRole()).isEqualTo(StudyRole.HOST);
    }

    @Test
    @DisplayName("존재하지 않는 스터디거나 스터디의 멤버가 아니면 예외가 발생한다.")
    void 스터디_멤버_아님_실패() {
        //given
        Long studyId = 999L;
        Long strangerId = 999L;

        given(studyMemberRepository.findByStudyIdAndMemberId(studyId, strangerId))
                .willReturn(Optional.empty());

        //when&then
        assertThatThrownBy(() -> studyDetailService.getStudyDetail(studyId, strangerId))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.NOT_STUDY_MEMBER);
    }
}
