package econovation.moongtaengi.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import econovation.moongtaengi.global.exception.ErrorCode;
import econovation.moongtaengi.global.exception.ErrorResponse;
import econovation.moongtaengi.global.security.exception.AuthErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 인증 실패 시 처리
 * 인증되지 않은 사용자가 보호된 리소스에 접근할 때 호출
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        log.warn("인증 실패 - URI: {}, Message: {}",
                request.getRequestURI(), authException.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(AuthErrorCode.UNAUTHORIZED);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
