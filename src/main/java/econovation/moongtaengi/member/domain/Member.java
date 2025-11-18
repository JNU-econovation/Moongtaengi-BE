package econovation.moongtaengi.member.domain;

import econovation.moongtaengi.global.entity.BaseEntity;
import jakarta.persistence.Column;
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

    @Column(nullable = false, unique = true)
    private String kakaoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    @Column(name = "interest_category_id")
    private Long interestCategoryId;

    // 카카오 로그인 시 임시 회원 생성
    public static Member createTemporaryMember(String kakaoId) {
        Member member = new Member();
        member.kakaoId = kakaoId;
        member.status = MemberStatus.TEMPORARY;
        return member;
    }

    public boolean isTemporary() {
        return this.status == MemberStatus.TEMPORARY;
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }
}
