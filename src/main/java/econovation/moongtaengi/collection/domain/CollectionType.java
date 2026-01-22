package econovation.moongtaengi.collection.domain;

import lombok.Getter;

/**
 * 컬렉션 타입 Enum
 * 각 컬렉션의 종류, 해금 조건, 이미지 URL을 정의
 * 이미지 URL은 Enum에서 직접 관리하여 정적 자산 관리를 단순화
 */
@Getter
public enum CollectionType {
    // 기본
    DEFAULT(
            "기본 뭉탱이",
            CollectionRarity.COMMON,
            "회원가입 시 기본 지급",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/default/default1.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/default/default2.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/default/default_bg.png"
    ),

    // 특별 코드 (스터디 참여 시 inviteCode로 해금)
    ECONO(
            "에코노 뭉탱이",
            CollectionRarity.COMMON,
            "에코노 코드로 스터디 참여",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/econo/econo1.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/econo/econo2.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/econo/econo_bg.png"
    ),

    SPECIAL(
            "스페셜 뭉탱이",
            CollectionRarity.RARE,
            "스페셜 코드로 스터디 참여",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/special/special1.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/special/special2.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/special/special_bg.png"
    ),

    // 경험치 기반
    PRO(
            "프로 뭉탱이",
            CollectionRarity.RARE,
            "100 XP 달성",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/pro/pro1.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/pro/pro2.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/pro/pro_bg.png"
    ),

    MASTER(
            "마스터 뭉탱이",
            CollectionRarity.EPIC,
            "300 XP 달성",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/master/master1.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/master/master2.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/master/master_bg.png"
    ),

    // 스터디 활동
    BUNCH(
            "뭉탱이 다발",
            CollectionRarity.EPIC,
            "4인 이상 스터디 참여",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/bunch/bunch1.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/bunch/bunch2.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/bunch/bunch_bg.png"
    ),

    // 컬렉션 수집
    TREASURE(
            "보물주머니 뭉탱이",
            CollectionRarity.UNIQUE,
            "7개 이상 컬렉션 해금",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/treasure/treasure1.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/treasure/treasure2.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/treasure/treasure_bg.png"
    ),

    // 온보딩 완료
    WOOD(
            "나무곡괭이 뭉탱이",
            CollectionRarity.COMMON,
            "모든 온보딩 미션 완료",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/wood/wood1.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/wood/wood2.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/wood/wood_bg.png"
    ),

    // 댓글 작성
    GRADUATION(
            "학사모 뭉탱이",
            CollectionRarity.EPIC,
            "댓글 10개 작성하기",
                    "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/graduation/graduation1.png",
                    "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/graduation/graduation2.png",
                    "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/graduation/graduation_bg.png"
    ),

    KANE(
            "케인 뭉탱이",
            CollectionRarity.UNIQUE,
            "댓글 100개 작성하기",
                    "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/kane/kane1.png",
                    "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/kane/kane2.png",
                    "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/kane/kane_bg.png"
    ),

    // 감정표현
    STICKER(
            "스티커 뭉탱이",
            CollectionRarity.RARE,
            "감정 표현 사용하기",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/sticker/sticker1.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/sticker/sticker2.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/sticker/sticker_bg.png"
    ),

    // 탑러너
    TOP_RUNNER(
            "탑러너 뭉탱이",
            CollectionRarity.UNIQUE,
            "탑러너에 입성하기",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/topRunner/topRunner1.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/topRunner/topRunner2.png",
            "https://moongtaengi-dev.s3.ap-northeast-2.amazonaws.com/collection/topRunner/topRunner_bg.png"
    );

    private final String displayName;
    private final CollectionRarity rarity;
    private final String description;
    private final String lockedImageUrl;      // 잠금 상태 이미지 (흐릿한 이미지)
    private final String unlockedImageUrl;    // 해금 상태 이미지 (원본 이미지)
    private final String backgroundImageUrl;  // 메인 페이지 배경용 이미지 (향후 사용)

    CollectionType(String displayName, CollectionRarity rarity, String description,
                   String lockedImageUrl, String unlockedImageUrl, String backgroundImageUrl) {
        this.displayName = displayName;
        this.rarity = rarity;
        this.description = description;
        this.lockedImageUrl = lockedImageUrl;
        this.unlockedImageUrl = unlockedImageUrl;
        this.backgroundImageUrl = backgroundImageUrl;
    }
}
