package econovation.moongtaengi.global.security;

import econovation.moongtaengi.global.security.config.JwtConfig;
import econovation.moongtaengi.global.security.exception.ExpiredTokenException;
import econovation.moongtaengi.global.security.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long accessTokenValidityMs;

    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.secret().getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidityMs = jwtConfig.accessTokenValidity();
        log.info("JwtTokenProvider 초기화 완료 - 만료시간: {}ms ({}분)",
                accessTokenValidityMs, accessTokenValidityMs / 60000);
    }

    public String createAccessToken(Long memberId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityMs);

        String token = Jwts.builder()
                .subject(String.valueOf(memberId))
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();

        log.debug("AccessToken 생성 - memberId: {}, 만료: {}", memberId, validity);
        return token;
    }

    public Long getMemberId(String token) {
        try {
            Claims claims = parseClaims(token);
            return Long.parseLong(claims.getSubject());
        } catch (NumberFormatException e) {
            log.error("memberId 파싱 실패: {}", token);
            throw new InvalidTokenException();
        }
    }

    public void validateToken(String token) {
        try {
            parseClaims(token);
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰: {}", e.getMessage());
            throw new ExpiredTokenException();
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰: {}", e.getMessage());
            throw new InvalidTokenException();
        } catch (MalformedJwtException e) {
            log.warn("잘못된 JWT 토큰: {}", e.getMessage());
            throw new InvalidTokenException();
        } catch (SignatureException e) {
            log.warn("JWT 서명 검증 실패: {}", e.getMessage());
            throw new InvalidTokenException();
        } catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 비어있음: {}", e.getMessage());
            throw new InvalidTokenException();
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
