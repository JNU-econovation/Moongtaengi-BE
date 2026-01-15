package econovation.moongtaengi.member.application;

import econovation.moongtaengi.member.domain.DailyQuest;
import econovation.moongtaengi.member.domain.DailyQuestRepository;
import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.MemberRepository;
import econovation.moongtaengi.member.domain.QuestType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExperienceService {
    private final MemberRepository memberRepository;
    private final DailyQuestRepository dailyQuestRepository;

    @Transactional
    public void completeQuest(Long memberId, QuestType questType) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException());

        DailyQuest dailyQuest = dailyQuestRepository
                .findByMemberIdAndQuestType(memberId, questType)
                .orElseGet(() -> DailyQuest.create(memberId, questType));

        if (!dailyQuest.canComplete()) {
            log.info("Member {} has already completed quest {} for today", memberId, questType);
            return;
        }

        dailyQuest.complete();
        member.addExperience(questType.getExperiencePoint());

        dailyQuestRepository.save(dailyQuest);
        memberRepository.save(member);

        log.info("Member {} completed quest {} and earned {} XP. Total XP: {}",
                memberId, questType, questType.getExperiencePoint(), member.getTotalExperience());
    }
}