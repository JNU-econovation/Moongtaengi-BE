package econovation.moongtaengi.study.infra;

import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.MemberRepository;
import econovation.moongtaengi.study.domain.StudyCreationValidator;
import econovation.moongtaengi.study.domain.StudyErrorCode;
import econovation.moongtaengi.study.domain.StudyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class StudyCreationValidatorImpl implements StudyCreationValidator {
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public void validate(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.CREATOR_NOT_FOUND));

        if (member.isTemporary()) {
            throw new StudyException(StudyErrorCode.STUDY_CREATION_DENIED_TEMP_MEMBER);
        }
    }
}
