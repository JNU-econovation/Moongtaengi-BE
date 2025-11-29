package econovation.moongtaengi.study.domain;

import econovation.moongtaengi.global.entity.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "studies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study extends BaseEntity {
    @Embedded
    StudyName name;

    public Study(StudyName name) {
        this.name = name;
    }
}
