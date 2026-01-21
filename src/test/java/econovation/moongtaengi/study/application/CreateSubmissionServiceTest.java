package econovation.moongtaengi.study.application;

import static econovation.moongtaengi.study.domain.submission.SubmissionFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import econovation.moongtaengi.study.application.submission.CreateSubmissionCommand;
import econovation.moongtaengi.study.application.submission.CreateSubmissionService;
import econovation.moongtaengi.study.domain.submission.AssignmentInfoProvider;
import econovation.moongtaengi.study.domain.submission.AssignmentInfoProvider.AssignmentInfo;
import econovation.moongtaengi.study.domain.submission.Submission;
import econovation.moongtaengi.study.domain.submission.SubmissionAuthorityValidator;
import econovation.moongtaengi.study.domain.submission.SubmissionRepository;
import econovation.moongtaengi.study.domain.submission.SubmissionUniquenessValidator;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class CreateSubmissionServiceTest {

    @Mock
    private SubmissionRepository submissionRepository;
    @Mock
    private AssignmentInfoProvider assignmentInfoProvider;
    @Mock
    private SubmissionAuthorityValidator authorityValidator;
    @Mock
    private SubmissionUniquenessValidator uniquenessValidator;

    @InjectMocks
    private CreateSubmissionService createSubmissionService;

    @Test
    @DisplayName("검증을 모두 통과하면 과제를 제출하고 저장한다")
    void 과제_제출_성공() {
        //given
        CreateSubmissionCommand command = new CreateSubmissionCommand(
                DEFAULT_ASSIGNMENT_ID,
                DEFAULT_SUBMITTER_ID,
                DEFAULT_CONTENT.getValue(),
                DEFAULT_ATTACHMENT.getName(),
                DEFAULT_ATTACHMENT.getUrl()
        );

        Submission savedSubmission = aSubmission().build();
        ReflectionTestUtils.setField(savedSubmission, "id", 1L);

        given(assignmentInfoProvider.getAssignmentInfo(DEFAULT_ASSIGNMENT_ID))
                .willReturn(new AssignmentInfo(DEFAULT_SUBMITTER_ID, BASE_TIME));

        given(submissionRepository.save(any(Submission.class))).willReturn(savedSubmission);

        //when
        Long resultId = createSubmissionService.createSubmission(command);

        //then
        assertThat(resultId).isEqualTo(1L);

        verify(assignmentInfoProvider).getAssignmentInfo(DEFAULT_ASSIGNMENT_ID);
        verify(authorityValidator).validate(DEFAULT_SUBMITTER_ID, DEFAULT_SUBMITTER_ID);
        verify(uniquenessValidator).validate(DEFAULT_ASSIGNMENT_ID);

        ArgumentCaptor<Submission> captor = ArgumentCaptor.forClass(Submission.class);
        verify(submissionRepository).save(captor.capture());

        Submission capturedSubmission = captor.getValue();

        assertThat(capturedSubmission.getAssignmentId()).isEqualTo(DEFAULT_ASSIGNMENT_ID);
        assertThat(capturedSubmission.getContent().getValue()).isEqualTo(DEFAULT_CONTENT.getValue());

        assertThat(capturedSubmission.getAttachment()).isPresent();
        assertThat(capturedSubmission.getAttachment().get().getName()).isEqualTo(DEFAULT_ATTACHMENT.getName());
        assertThat(capturedSubmission.getAttachment().get().getUrl()).isEqualTo(DEFAULT_ATTACHMENT.getUrl());
    }
}
