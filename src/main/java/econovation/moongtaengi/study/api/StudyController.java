package econovation.moongtaengi.study.api;

import econovation.moongtaengi.global.annotation.LoginMemberId;
import econovation.moongtaengi.study.api.dto.StudyCreateRequest;
import econovation.moongtaengi.study.api.dto.StudyJoinRequest;
import econovation.moongtaengi.study.application.CreateStudyService;
import econovation.moongtaengi.study.application.JoinStudyService;
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
    private final JoinStudyService joinStudyService;

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

    @PostMapping("/join")
    public ResponseEntity<Void> joinStudy(
            @LoginMemberId Long memberId,
            @RequestBody @Valid StudyJoinRequest request) {
        log.info("스터디 가입 API 호출 - memberId: {}, inviteCode: {}",
                memberId, request.inviteCode());

        joinStudyService.joinStudy(memberId, request.inviteCode());

        return ResponseEntity.ok().build();
    }
}
