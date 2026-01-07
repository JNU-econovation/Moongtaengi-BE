package econovation.moongtaengi.study.api;

import econovation.moongtaengi.global.annotation.LoginMemberId;
import econovation.moongtaengi.study.api.dto.StudySummaryResponse;
import econovation.moongtaengi.study.application.StudyQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies/me")
public class MyStudyController {
    private final StudyQueryService studyQueryService;

    @GetMapping("/managed")
    public ResponseEntity<List<StudySummaryResponse>> getManagedStudies(
            @LoginMemberId Long memberId) {
        log.info("내가 운영 중인 스터디 목록 조회 API 호출 - memberId: {}", memberId);
        return ResponseEntity.ok(studyQueryService.getManagedStudies(memberId));
    }

    @GetMapping("/joined")
    public ResponseEntity<List<StudySummaryResponse>> getJoinedStudies(
            @LoginMemberId Long memberId) {
        log.info("내가 참여 중인 스터디 목록 조회 API 호출 - memberId: {}", memberId);
        return ResponseEntity.ok(studyQueryService.getJoinedStudies(memberId));
    }
}
