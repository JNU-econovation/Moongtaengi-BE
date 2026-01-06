package econovation.moongtaengi.study.infra;

import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.MemberRepository;
import econovation.moongtaengi.study.domain.Study;
import econovation.moongtaengi.study.domain.StudyErrorCode;
import econovation.moongtaengi.study.domain.StudyException;
import econovation.moongtaengi.study.domain.StudyJoinValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudyJoinValidatorImpl implements StudyJoinValidator {
    private final MemberRepository memberRepository;

    @Override
    public void validate(Long memberId, Study study) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.JOINER_NOT_FOUND));

        if (member.isTemporary()) {
            throw new StudyException(StudyErrorCode.STUDY_JOIN_DENIED_TEMP_MEMBER);
        }
    }
}
