package econovation.moongtaengi.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Title {
    BEGINNER("BEGINNER", 0),
    PRO("PRO", 100),
    MASTER("MASTER", 300);

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
