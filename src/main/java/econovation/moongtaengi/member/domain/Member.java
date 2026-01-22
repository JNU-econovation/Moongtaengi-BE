package econovation.moongtaengi.member.domain;

import econovation.moongtaengi.collection.domain.CollectionType;
import econovation.moongtaengi.global.entity.BaseEntity;
import econovation.moongtaengi.member.domain.event.ExperienceAddedEvent;
import econovation.moongtaengi.member.domain.event.LoginSuccessEvent;
import econovation.moongtaengi.member.domain.event.MemberRegisteredEvent;
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

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int totalExperience = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CollectionType profileIcon = CollectionType.DEFAULT;

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
        registerEvent(new MemberRegisteredEvent(this.getId()));
        registerEvent(new LoginSuccessEvent(this.getId()));
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

    public Title getTitle() {
        return Title.fromExperience(this.totalExperience);
    }

    public void addExperience(int experience) {
        if (experience <= 0) {
            throw new IllegalArgumentException("경험치는 0보다 커야 합니다.");
        }
        this.totalExperience += experience;
        registerEvent(new ExperienceAddedEvent(this.getId(), experience, this.totalExperience));
    }

    public void changeProfileIcon(CollectionType type) {
        if (type == null) {
            throw new IllegalArgumentException("프로필 아이콘은 null일 수 없습니다.");
        }
        this.profileIcon = type;
    }

    public void updateNickname(Nickname nickname) {
        if (nickname == null) {
            throw new IllegalArgumentException("닉네임은 null일 수 없습니다.");
        }
        this.nickname = nickname;
    }
}
