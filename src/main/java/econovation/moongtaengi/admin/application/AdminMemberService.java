package econovation.moongtaengi.admin.application;

import econovation.moongtaengi.admin.api.dto.AdminMemberResponse;
import econovation.moongtaengi.admin.api.dto.CreateMemberRequest;
import econovation.moongtaengi.admin.domain.AdminErrorCode;
import econovation.moongtaengi.admin.domain.AdminException;
import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.MemberRepository;
import econovation.moongtaengi.member.domain.Nickname;
import econovation.moongtaengi.study.domain.StudyMemberRepository;
import java.util.Random;
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
    private final StudyMemberRepository studyMemberRepository;

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

        // 3. 회원과 관련된 스터디 삭제
        int deletedStudyCount = studyMemberRepository
                .findAllByMemberId(memberId).size();

        studyMemberRepository.deleteAllByMemberId(memberId);

        log.info("회원과 관련된 스터디 삭제 완료 - memberId: {}, 삭제된 스터디: {}개",
                memberId, deletedStudyCount);

        // 4. 회원 삭제
        memberRepository.delete(member);

        log.info("✅ 회원 삭제 완료 - memberId: {}, 함께 삭제된 스터디 수: {}개",
                memberId, deletedStudyCount);

    }

    /**
     * Mock 회원 10명 생성
     */
    public int createMockMembers() {
        log.info("Mock 회원 10명 생성 시작");

        String[] prefixes = {
                "귀여운", "멋진", "행복한", "용감한", "똑똑한",
                "친절한", "활발한", "조용한", "신비한", "재밌는"
        };

        String[] animals = {
                "토끼", "사자", "곰", "여우", "판다",
                "고양이", "강아지", "호랑이", "펭귄", "코알라"
        };

        Random random = new Random();

        int createdCount = 0;
        long timestamp = System.currentTimeMillis();

        for (int i = 0; i < 10; i++) {
            // 접두사(3글자) + 동물(2글자) = 5글자
            String prefix = prefixes[random.nextInt(prefixes.length)];
            String animal = animals[random.nextInt(animals.length)];
            String baseNickname = prefix + animal;  // 예: "귀여운토끼" (5글자)

            String finalNickname = baseNickname;
            int attempt = 1;

            while (memberRepository.existsByNicknameValue(finalNickname)) {
                finalNickname = baseNickname + attempt; // 예: 귀여운펭귄1
                attempt++;
            }

            String kakaoId = "mock-user-" + timestamp + "-" + i;

            try {
                CreateMemberRequest request = new CreateMemberRequest(kakaoId, finalNickname);
                createMember(request);
                createdCount++;

                log.debug("Mock 회원 생성: {} ({}글자)", finalNickname, finalNickname.length());

            } catch (Exception e) {
                log.warn("Mock 회원 생성 실패: {}, error: {}", finalNickname, e.getMessage());
            }
        }

        log.info("✅ Mock 회원 {}명 생성 완료", createdCount);
        return createdCount;
    }


    /**
     * 전체 회원 수 조회
     */
    public long getMemberCount() {
        return memberRepository.count();
    }
}
