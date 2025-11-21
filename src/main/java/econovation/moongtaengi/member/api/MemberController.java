package econovation.moongtaengi.member.api;

import econovation.moongtaengi.member.api.dto.NicknameCheckResponse;
import econovation.moongtaengi.member.application.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/check-nickname")
    public ResponseEntity<NicknameCheckResponse> checkNickname(@RequestParam("nickname") String nickname) {
        memberService.checkNicknameAvailability(nickname);

        return ResponseEntity.ok(NicknameCheckResponse.available(nickname));
    }

    @ExceptionHandler(IllegalArgumentException.class) //예외처리기 PR 머지 후 커스텀 예외 클래스로 변경 예정
    public ResponseEntity<NicknameCheckResponse> handleIllegalArgumentException(
            IllegalArgumentException e,
            HttpServletRequest request
    ) {
        String nickname = request.getParameter("nickname");

        return ResponseEntity.ok(
                NicknameCheckResponse.unavailable(nickname, e.getMessage())
        );
    }
}
