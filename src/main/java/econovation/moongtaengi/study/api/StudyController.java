package econovation.moongtaengi.study.api;

import econovation.moongtaengi.global.annotation.LoginMemberId;
import econovation.moongtaengi.study.api.dto.StudyCreateRequest;
import econovation.moongtaengi.study.application.CreateStudyService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies")
public class StudyController {
    private final CreateStudyService createStudyService;

    @PostMapping
    public ResponseEntity<Void> createStudy(
            @LoginMemberId Long memberId,
            @RequestBody @Valid StudyCreateRequest request) {
        log.info("스터디 생성 API 호출 - memberId: {}, studyName: {}, studyTopic: {}",
                memberId, request.name(), request.topic());

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
