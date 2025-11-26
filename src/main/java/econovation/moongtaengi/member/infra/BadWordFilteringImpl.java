package econovation.moongtaengi.member.infra;

import com.vane.badwordfiltering.BadWordFiltering;
import econovation.moongtaengi.member.domain.BadWordValidator;
import org.springframework.stereotype.Component;

@Component
public class BadWordFilteringImpl implements BadWordValidator {
    private static final BadWordFiltering badWordFiltering = new BadWordFiltering();

    @Override
    public boolean containsBadWord(String word) {
        return badWordFiltering.blankCheck(word);
    }
}
