package econovation.moongtaengi.global.security;

import econovation.moongtaengi.global.security.exception.ExpiredTokenException;
import econovation.moongtaengi.global.security.exception.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String token = resolveToken(request);

            if (StringUtils.hasText(token)) {
                jwtTokenProvider.validateToken(token);
                Long memberId = jwtTokenProvider.getMemberId(token);

                CustomAuthentication authentication = new CustomAuthentication(memberId);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("JWT 인증 성공 - memberId: {}, URI: {}", memberId, request.getRequestURI());
            }

        } catch (InvalidTokenException | ExpiredTokenException e) {
            log.warn("JWT 인증 실패: {} - URI: {}", e.getMessage(), request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }
}
