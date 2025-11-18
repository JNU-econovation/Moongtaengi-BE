package econovation.moongtaengi.interest.api;

import econovation.moongtaengi.interest.api.dto.InterestCategoryResponse;
import econovation.moongtaengi.interest.application.InterestCategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interest")
@RequiredArgsConstructor
public class InterestCategoryController {

    private final InterestCategoryService interestCategoryService;

    @GetMapping
    public ResponseEntity<List<InterestCategoryResponse>> getInterestCategories() {
        List<InterestCategoryResponse> categories =
                interestCategoryService.getAllInterestCategories()
                        .stream()
                        .map(InterestCategoryResponse::from)
                        .toList();

        return ResponseEntity.ok(categories);
    }
}
