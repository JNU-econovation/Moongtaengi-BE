package econovation.moongtaengi.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Title {
    BEGINNER("비기너", 0),
    PRO("프로", 100),
    MASTER("마스터", 300);

    private final String displayName;
    private final int requiredExperience;

    public static Title fromExperience(int experience) {
        if (experience >= MASTER.requiredExperience) {
            return MASTER;
        }
        if (experience >= PRO.requiredExperience) {
            return PRO;
        }
        return BEGINNER;
    }
}