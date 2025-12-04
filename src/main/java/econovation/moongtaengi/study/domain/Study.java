package econovation.moongtaengi.study.domain;

import econovation.moongtaengi.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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

    @Embedded
    StudyPeriod period;

    @Embedded
    StudyTopic topic;

    @Embedded
    InviteCode inviteCode;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyMember> members = new ArrayList<>();

    public Study(StudyName name, StudyPeriod period, StudyTopic topic, Long hostId, InviteCode inviteCode) {
        this.name = name;
        this.period = period;
        this.topic = topic;
        this.inviteCode = inviteCode;
        addMember(hostId, StudyRole.HOST); //스터디를 생성한 사람은 방장
    }

    public void addMember(Long memberId, StudyRole studyRole) {
        StudyMember studyMember = new StudyMember(this, memberId, studyRole);
        members.add(studyMember);
    }
}
