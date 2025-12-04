package econovation.moongtaengi.member.api;

import econovation.moongtaengi.member.api.dto.CompleteRegistrationRequest;
import econovation.moongtaengi.member.api.dto.NicknameCheckResponse;
import econovation.moongtaengi.member.application.MemberService;
import econovation.moongtaengi.member.domain.NicknameException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/check-nickname")
    public ResponseEntity<NicknameCheckResponse> checkNickname(@RequestParam("nickname") String nickname) {
        try {
            memberService.checkNicknameAvailability(nickname);
            return ResponseEntity.ok(NicknameCheckResponse.available(nickname));
        } catch (NicknameException e) {
            return ResponseEntity.ok(NicknameCheckResponse.unavailable(nickname, e.getErrorCode()));
        }
    }

    @PostMapping("/me/complete-registration")
    public ResponseEntity<Void> completeRegistration(
            @Valid @RequestBody CompleteRegistrationRequest request
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = (Long) auth.getPrincipal();

        memberService.completeRegistration(memberId, request.nickname());

        log.info("회원가입 완료 API 호출 성공 - memberId: {}", memberId);

        return ResponseEntity.ok().build();
    }

}
