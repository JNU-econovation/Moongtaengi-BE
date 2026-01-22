package econovation.moongtaengi.study.application.submission;

import econovation.moongtaengi.study.domain.submission.Submission;
import econovation.moongtaengi.study.domain.submission.SubmissionAttachment;
import econovation.moongtaengi.study.domain.submission.SubmissionContent;
import econovation.moongtaengi.study.domain.submission.SubmissionErrorCode;
import econovation.moongtaengi.study.domain.submission.SubmissionException;
import econovation.moongtaengi.study.domain.submission.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateSubmissionService {

    private final SubmissionRepository submissionRepository;

    @Transactional
    public void updateSubmission(UpdateSubmissionCommand command) {

        Submission submission = submissionRepository.findById(command.submissionId())
                .orElseThrow(() -> new SubmissionException(SubmissionErrorCode.SUBMISSION_NOT_FOUND));

        SubmissionContent contentToUpdate = command.content() != null
                ? new SubmissionContent(command.content())
                : submission.getContent();

        SubmissionAttachment attachmentToUpdate = submission.getAttachment().orElse(null);

        if (command.fileName() != null && command.fileUrl() != null) {
            attachmentToUpdate = SubmissionAttachment.of(command.fileName(), command.fileUrl());
        }

        submission.update(command.requesterId(), contentToUpdate, attachmentToUpdate);

        log.info("과제 제출 수정 완료 - submissionId: {}, requesterId: {}",
                submission.getId(), command.requesterId());
    }

}
