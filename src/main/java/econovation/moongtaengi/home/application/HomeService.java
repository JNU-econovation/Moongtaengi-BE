package econovation.moongtaengi.home.application;

import econovation.moongtaengi.collection.application.CollectionService;
import econovation.moongtaengi.home.api.dto.HomeResponse;
import econovation.moongtaengi.home.api.dto.TopRunnerInfo;
import econovation.moongtaengi.member.application.MemberNotFoundException;
import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.MemberRepository;
import econovation.moongtaengi.study.domain.StudyMember;
import econovation.moongtaengi.study.domain.StudyMemberRepository;
import econovation.moongtaengi.study.domain.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * 메인 페이지 서비스
 * 사용자 정보 및 탑러너 정보 제공
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    private final MemberRepository memberRepository;
    private final CollectionService collectionService;
    private final CommentRepository commentRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final Random random = new Random();

    private static final List<String> CHEERING_MESSAGES = List.of(
            "오늘도 화이팅!",
            "뭉탱이와 함께 달려요!",
            "꾸준함이 답이다!",
            "당신은 할 수 있어요!",
            "조금씩 성장하고 있어요!",
            "멋진 하루 되세요!",
            "함께 공부해요!",
            "오늘도 좋은 하루!",
            "열심히 하는 당신, 멋져요!",
            "작은 노력이 모여 큰 결과를 만들어요!"
    );

    /**
     * 메인 페이지 정보 조회
     * @param memberId 회원 ID
     * @return 메인 페이지 응답
     */
    public HomeResponse getHomeInfo(Long memberId) {
        // 회원 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        // 랜덤 응원 메시지 선택
        String cheeringMessage = CHEERING_MESSAGES.get(random.nextInt(CHEERING_MESSAGES.size()));

        // 댓글 수 조회
        long commentCount = commentRepository.countByMemberId(memberId);

        // 컬렉션 수 조회
        long collectionCount = collectionService.getCollectionCount(memberId);

        // 바로가기 스터디 ID 조회 (가장 최근 업데이트된 스터디)
        Long shortcutStudyId = studyMemberRepository
                .findMostRecentStudyByMemberId(memberId, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .map(StudyMember::getStudy)
                .map(study -> study.getId())
                .orElse(null);

        // 탑러너 목록 조회
        List<TopRunnerInfo> topRunners = memberRepository
                .findTopMembersByExperience(PageRequest.of(0, 5))
                .stream()
                .map(topMember -> new TopRunnerInfo(
                        topMember.getNickname().getValue(),
                        topMember.getProfileIcon().getUnlockedImageUrl()
                ))
                .toList();

        log.info("회원 {}의 메인 페이지 정보를 조회했습니다", memberId);

        return new HomeResponse(
                member.getNickname().getValue(),
                member.getProfileIcon().getDisplayName(),
                member.getProfileIcon().getUnlockedImageUrl(),
                member.getProfileIcon().getBackgroundImageUrl(),
                cheeringMessage,
                commentCount,
                member.getTitle().getDisplayName(),
                collectionCount,
                member.getTotalExperience(),
                shortcutStudyId,
                topRunners
        );
    }
}
