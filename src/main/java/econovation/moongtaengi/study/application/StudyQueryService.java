package econovation.moongtaengi.study.application;

import econovation.moongtaengi.study.api.dto.StudySummaryResponse;
import econovation.moongtaengi.study.domain.StudyMemberRepository;
import econovation.moongtaengi.study.domain.StudyRole;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyQueryService {
    private final StudyMemberRepository studyMemberRepository;

    public List<StudySummaryResponse> getManagedStudies(Long memberId) {
        return studyMemberRepository.findAllByMemberIdAndRole(memberId, StudyRole.HOST)
                .stream()
                .map(studyMember -> StudySummaryResponse.from(studyMember.getStudy()))
                .toList();
    }

    public List<StudySummaryResponse> getJoinedStudies(Long memberId) {
        return studyMemberRepository.findAllByMemberIdAndRole(memberId, StudyRole.GUEST)
                .stream()
                .map(studyMember -> StudySummaryResponse.from(studyMember.getStudy()))
                .toList();
    }
}
