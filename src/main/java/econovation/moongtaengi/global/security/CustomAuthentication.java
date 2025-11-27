package econovation.moongtaengi.global.security;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * JWT 인증 전용 Authentication
 * 단순히 memberId만 저장
 */
public class CustomAuthentication implements Authentication {

    private final Long memberId;
    private boolean authenticated;

    public CustomAuthentication(Long memberId) {
        this.memberId = memberId;
        this.authenticated = true;
    }

    @Override
    public Long getPrincipal() {
        return memberId;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();  // 권한 없음
    }

    @Override
    public Object getCredentials() {
        return null;  // 비밀번호 없음
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public String getName() {
        return String.valueOf(memberId);
    }
}
