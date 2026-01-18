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
            "https://s3.ap-northeast-2.amazonaws.com/moongtaengi-bucket/collections/default.png"
    ),

    // 특별 코드 (스터디 참여 시 inviteCode로 해금)
    ECONO_SPECIAL(
            "에코노 뭉탱이",
            CollectionRarity.COMMON,
            "에코노 코드로 스터디 참여",
            "https://s3.ap-northeast-2.amazonaws.com/moongtaengi-bucket/collections/econo-special.png"
    ),
    SPECIAL(
            "스페셜 뭉탱이",
            CollectionRarity.RARE,
            "스페셜 코드로 스터디 참여",
            "https://s3.ap-northeast-2.amazonaws.com/moongtaengi-bucket/collections/special.png"
    ),

    // 경험치 기반
    PRO(
            "프로 뭉탱이",
            CollectionRarity.RARE,
            "100 XP 달성",
            "https://s3.ap-northeast-2.amazonaws.com/moongtaengi-bucket/collections/pro.png"
    ),
    MASTER(
            "마스터 뭉탱이",
            CollectionRarity.EPIC,
            "300 XP 달성",
            "https://s3.ap-northeast-2.amazonaws.com/moongtaengi-bucket/collections/master.png"
    ),

    // 스터디 활동
    BUNCH(
            "뭉탱이 다발",
            CollectionRarity.EPIC,
            "4인 이상 스터디 참여",
            "https://s3.ap-northeast-2.amazonaws.com/moongtaengi-bucket/collections/bunch.png"
    ),

    // 컬렉션 수집
    TREASURE_BAG(
            "보물주머니 뭉탱이",
            CollectionRarity.UNIQUE,
            "7개 이상 컬렉션 해금",
            "https://s3.ap-northeast-2.amazonaws.com/moongtaengi-bucket/collections/treasure-bag.png"
    ),

    // 온보딩 완료
    WOODEN_PICKAXE(
            "나무곡괭이 뭉탱이",
            CollectionRarity.COMMON,
            "모든 온보딩 미션 완료",
            "https://s3.ap-northeast-2.amazonaws.com/moongtaengi-bucket/collections/wooden-pickaxe.png"
    );

    private final String displayName;
    private final CollectionRarity rarity;
    private final String description;
    private final String imageUrl;

    CollectionType(String displayName, CollectionRarity rarity, String description, String imageUrl) {
        this.displayName = displayName;
        this.rarity = rarity;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}
