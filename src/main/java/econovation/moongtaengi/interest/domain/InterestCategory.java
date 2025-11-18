package econovation.moongtaengi.interest.domain;

import econovation.moongtaengi.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "interest_categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterestCategory extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String displayName;

    public static InterestCategory create(String displayName) {
        InterestCategory category = new InterestCategory();
        category.displayName = displayName;
        return category;
    }
}
