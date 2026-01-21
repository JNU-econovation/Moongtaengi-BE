package econovation.moongtaengi.study.application.submission;

import econovation.moongtaengi.study.domain.submission.AssignmentInfoProvider;
import econovation.moongtaengi.study.domain.submission.AssignmentInfoProvider.AssignmentInfo;
import econovation.moongtaengi.study.domain.submission.Submission;
import econovation.moongtaengi.study.domain.submission.SubmissionAttachment;
import econovation.moongtaengi.study.domain.submission.SubmissionAuthorityValidator;
import econovation.moongtaengi.study.domain.submission.SubmissionContent;
import econovation.moongtaengi.study.domain.submission.SubmissionRepository;
import econovation.moongtaengi.study.domain.submission.SubmissionUniquenessValidator;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateSubmissionService {
    private final SubmissionRepository submissionRepository;
    private final AssignmentInfoProvider assignmentInfoProvider;
    private final SubmissionAuthorityValidator authorityValidator;
    private final SubmissionUniquenessValidator uniquenessValidator;

    @Transactional
    public Long createSubmission(CreateSubmissionCommand command) {
        AssignmentInfo assignmentInfo = assignmentInfoProvider.getAssignmentInfo(command.assignmentId());

        authorityValidator.validate(command.submitterId(), assignmentInfo.assigneeId());

        uniquenessValidator.validate(command.assignmentId());

        SubmissionContent content = new SubmissionContent(command.content());

        SubmissionAttachment attachment = null;
        if (command.hasAttachment()) {
            attachment = SubmissionAttachment.of(command.fileName(), command.fileUrl());
        }

        Submission submission = Submission.builder()
                .assignmentId(command.assignmentId())
                .submitterId(command.submitterId())
                .content(content)
                .currentDateTime(LocalDateTime.now())
                .attachment(attachment)
                .assignmentDeadline(assignmentInfo.deadline())
                .build();

        Submission savedSubmission = submissionRepository.save(submission);

        log.info("과제 제출 완료 - assignmentId: {}, submissionId: {}, isLate: {}",
                command.assignmentId(), savedSubmission.getId(), savedSubmission.isLate());

        return savedSubmission.getId();
    }
}
