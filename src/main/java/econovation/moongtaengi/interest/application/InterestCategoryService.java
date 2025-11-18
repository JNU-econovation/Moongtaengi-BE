package econovation.moongtaengi.interest.application;

import econovation.moongtaengi.interest.domain.InterestCategory;
import econovation.moongtaengi.interest.domain.InterestCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InterestCategoryService {

    private final InterestCategoryRepository interestCategoryRepository;

    public List<InterestCategory> getAllInterestCategories() {
        return interestCategoryRepository.findAll();

    }

}
