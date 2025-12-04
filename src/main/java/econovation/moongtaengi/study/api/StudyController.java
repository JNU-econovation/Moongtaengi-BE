package econovation.moongtaengi.study.api;

import econovation.moongtaengi.global.annotation.LoginMemberId;
import econovation.moongtaengi.study.api.dto.StudyCreateRequest;
import econovation.moongtaengi.study.application.CreateStudyService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies")
public class StudyController {
    private final CreateStudyService createStudyService;

    @PostMapping
    public ResponseEntity<Void> createStudy(
            @LoginMemberId Long memberId,
            @RequestBody @Valid StudyCreateRequest request) {
        Long studyId = createStudyService.createStudy(
                memberId,
                request.name(),
                request.topic(),
                request.startDate(),
                request.endDate()
        );

        return ResponseEntity.created(URI.create("/api/studies/" + studyId)).build();
    }
}
