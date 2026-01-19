package econovation.moongtaengi.study.domain.comment;

public interface CommentMemberValidator {

    void validate(Long memberId, Long submissionId);
}
