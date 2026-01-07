package econovation.moongtaengi.study.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import econovation.moongtaengi.study.api.dto.StudySummaryResponse;
import econovation.moongtaengi.study.domain.InviteCode;
import econovation.moongtaengi.study.domain.Study;
import econovation.moongtaengi.study.domain.StudyMember;
import econovation.moongtaengi.study.domain.StudyMemberRepository;
import econovation.moongtaengi.study.domain.StudyName;
import econovation.moongtaengi.study.domain.StudyPeriod;
import econovation.moongtaengi.study.domain.StudyRole;
import econovation.moongtaengi.study.domain.StudyTopic;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class StudyQueryServiceTest {
    @InjectMocks
    private StudyQueryService studyQueryService;

    @Mock
    private StudyMemberRepository studyMemberRepository;

    @Test
    @DisplayName("내가 관리하는 스터디 목록을 조회한다.")
    void 나의_관리_스터디_조회_성공() {
        // given
        Long memberId = 1L;
        Long studyId = 100L;

        Study study = new Study(
                new StudyName("테스트 스터디"),
                new StudyPeriod(LocalDate.now(), LocalDate.now().plusDays(7)),
                new StudyTopic("테스트 주제"),
                memberId,
                new InviteCode("12345678")
        );
        ReflectionTestUtils.setField(study, "id", studyId);

        StudyMember studyMember = new StudyMember(
                study,
                memberId,
                StudyRole.HOST);

        given(studyMemberRepository.findAllByMemberIdAndRole(memberId, StudyRole.HOST))
                .willReturn(List.of(studyMember));

        // when
        List<StudySummaryResponse> result = studyQueryService.getManagedStudies(memberId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().studyName()).isEqualTo("테스트 스터디");
        assertThat(result.getFirst().studyId()).isEqualTo(studyId);
        verify(studyMemberRepository).findAllByMemberIdAndRole(memberId, StudyRole.HOST);
    }

    @Test
    @DisplayName("내가 참여 중인 스터디 목록을 조회한다.")
    void 나의_참여_스터디_조회_성공() {
        //given
        Long memberId = 1L;
        Long studyId = 100L;

        Study study = new Study(
                new StudyName("참여하는 스터디"),
                new StudyPeriod(LocalDate.now(), LocalDate.now().plusDays(7)),
                new StudyTopic("스터디 주제"),
                10L,
                new InviteCode("12345678")
        );
        ReflectionTestUtils.setField(study, "id", studyId);

        StudyMember studyMember = new StudyMember(
                study,
                memberId,
                StudyRole.GUEST
        );

        given(studyMemberRepository.findAllByMemberIdAndRole(memberId, StudyRole.GUEST))
                .willReturn(List.of(studyMember));

        //when
        List<StudySummaryResponse> result = studyQueryService.getJoinedStudies(memberId);

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().studyName()).isEqualTo("참여하는 스터디");
        assertThat(result.getFirst().studyId()).isEqualTo(studyId);
        verify(studyMemberRepository).findAllByMemberIdAndRole(memberId, StudyRole.GUEST);
    }
}
