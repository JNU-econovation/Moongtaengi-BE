package econovation.moongtaengi.study.api;

import econovation.moongtaengi.global.annotation.LoginMemberId;
import econovation.moongtaengi.study.api.dto.StudySummaryResponse;
import econovation.moongtaengi.study.application.StudyQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies/me")
public class MyStudyController {
    private final StudyQueryService studyQueryService;

    @GetMapping("/managed")
    public ResponseEntity<List<StudySummaryResponse>> getManagedStudies(
            @LoginMemberId Long memberId) {
        return ResponseEntity.ok(studyQueryService.getManagedStudies(memberId));
    }

    @GetMapping("/joined")
    public ResponseEntity<List<StudySummaryResponse>> getJoinedStudies(
            @LoginMemberId Long memberId) {
        return ResponseEntity.ok(studyQueryService.getJoinedStudies(memberId));
    }
}
