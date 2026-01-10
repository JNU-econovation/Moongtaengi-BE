package econovation.moongtaengi.admin.application;

import econovation.moongtaengi.admin.api.dto.AdminMemberResponse;
import econovation.moongtaengi.admin.api.dto.CreateMemberRequest;
import econovation.moongtaengi.admin.domain.AdminErrorCode;
import econovation.moongtaengi.admin.domain.AdminException;
import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.MemberRepository;
import econovation.moongtaengi.member.domain.Nickname;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 목록 조회 (페이징)
     */
    public Page<AdminMemberResponse> getMembers(Pageable pageable) {
        log.info("회원 목록 조회 - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        return memberRepository.findAll(pageable)
                .map(AdminMemberResponse::from);
    }

    /**
     * 회원 상세 조회
     */
    public AdminMemberResponse getMember(Long memberId) {
        log.info("회원 상세 조회 - memberId: {}", memberId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.MEMBER_NOT_FOUND));

        return AdminMemberResponse.from(member);
    }

    /**
     * 회원 생성
     */
    @Transactional
    public AdminMemberResponse createMember(CreateMemberRequest request) {
        log.info("회원 생성 - kakaoId: {}, nickname: {}",
                request.kakaoId(), request.nickname());

        // 1. 중복 확인
        if (memberRepository.existsByKakaoId(request.kakaoId())) {
            log.warn("이미 존재하는 카카오 ID - kakaoId: {}", request.kakaoId());
            throw new AdminException(AdminErrorCode.MEMBER_ALREADY_EXISTS);
        }

        // 2. 회원 생성
        Nickname nickname = new Nickname(request.nickname());
        Member member = Member.createMember(request.kakaoId(), nickname);

        Member savedMember = memberRepository.save(member);

        log.info("✅ 회원 생성 완료 - memberId: {}", savedMember.getId());

        return AdminMemberResponse.from(savedMember);
    }

    /**
     * 회원 삭제
     */
    @Transactional
    public void deleteMember(Long memberId) {
        log.info("회원 삭제 - memberId: {}", memberId);

        // 1. 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.MEMBER_NOT_FOUND));

        // 2. 관리자는 삭제 불가
        if (member.isAdmin()) {
            log.warn("관리자 계정 삭제 시도 - memberId: {}", memberId);
            throw new AdminException(AdminErrorCode.CANNOT_DELETE_ADMIN);
        }

        // 3. 삭제
        memberRepository.delete(member);

        log.info("✅ 회원 삭제 완료 - memberId: {}", memberId);
    }

    /**
     * 전체 회원 수 조회
     */
    public long getMemberCount() {
        return memberRepository.count();
    }
}
