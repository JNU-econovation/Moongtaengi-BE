package econovation.moongtaengi.gamification.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyQuestRepository extends JpaRepository<DailyQuest, Long> {
    Optional<DailyQuest> findByMemberIdAndQuestType(Long memberId, QuestType questType);

    List<DailyQuest> findAllByMemberId(Long memberId);
}