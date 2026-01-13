package econovation.moongtaengi.member.domain;

import econovation.moongtaengi.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Embedded
    private Nickname nickname;

    @Column(nullable = false, unique = true)
    private String kakaoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'USER'")
    private Role role = Role.USER;

    // 카카오 로그인 시 임시 회원 생성
    public static Member createTemporaryMember(String kakaoId) {
        Member member = new Member();
        member.kakaoId = kakaoId;
        member.status = MemberStatus.TEMPORARY;
        return member;
    }

    public void completeRegistration(Nickname nickname) {
        validateCanComplete();
        this.nickname = nickname;
        this.status = MemberStatus.ACTIVE;
    }

    private void validateCanComplete() {
        if (this.status == MemberStatus.ACTIVE) {
            throw new AlreadyRegisteredException();
        }
    }

    // 일반 회원 생성 (관리자가 직접 생성)
    public static Member createMember(String kakaoId, Nickname nickname) {
        Member member = new Member();
        member.kakaoId = kakaoId;
        member.nickname = nickname;
        member.status = MemberStatus.ACTIVE;
        member.role = Role.USER;
        return member;
    }

    public boolean isTemporary() {
        return this.status == MemberStatus.TEMPORARY;
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    public boolean isUser() {
        return this.role == Role.USER;
    }
}
