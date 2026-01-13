package econovation.moongtaengi.admin.application;

import econovation.moongtaengi.admin.api.dto.AddStudyMemberRequest;
import econovation.moongtaengi.admin.api.dto.AdminStudyResponse;
import econovation.moongtaengi.admin.api.dto.AvailableMemberResponse;
import econovation.moongtaengi.admin.api.dto.CreateStudyRequest;
import econovation.moongtaengi.admin.domain.AdminErrorCode;
import econovation.moongtaengi.admin.domain.AdminException;
import econovation.moongtaengi.member.domain.Member;
import econovation.moongtaengi.member.domain.MemberRepository;
import econovation.moongtaengi.member.domain.MemberStatus;
import econovation.moongtaengi.study.domain.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminStudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;

    /**
     * 스터디 목록 조회 (페이징)
     */
    public Page<AdminStudyResponse> getStudies(Pageable pageable) {
        log.info("스터디 목록 조회 - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        return studyRepository.findAll(pageable)
                .map(AdminStudyResponse::from);
    }

    /**
     * 스터디 상세 조회
     */
    public AdminStudyResponse getStudy(Long studyId) {
        log.info("스터디 상세 조회 - studyId: {}", studyId);

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.STUDY_NOT_FOUND));

        return AdminStudyResponse.from(study);
    }

    /**
     * 스터디 생성
     */
    @Transactional
    public AdminStudyResponse createStudy(CreateStudyRequest request) {
        log.info("스터디 생성 - name: {}, hostId: {}",
                request.name(), request.hostId());

        // 1. 호스트 존재 확인
        if (!memberRepository.existsById(request.hostId())) {
            log.warn("존재하지 않는 호스트 - hostId: {}", request.hostId());
            throw new AdminException(AdminErrorCode.HOST_NOT_FOUND);
        }

        // 2. 스터디 생성
        StudyName name = new StudyName(request.name());
        StudyPeriod period = new StudyPeriod(request.startDate(), request.endDate());
        StudyTopic topic = new StudyTopic(request.topic());
        InviteCode inviteCode = generateUniqueInviteCode();

        Study study = new Study(name, period, topic, request.hostId(), inviteCode);

        Study savedStudy = studyRepository.save(study);

        log.info("✅ 스터디 생성 완료 - studyId: {}", savedStudy.getId());

        return AdminStudyResponse.from(savedStudy);
    }

    /**
     * 스터디 삭제
     */
    @Transactional
    public void deleteStudy(Long studyId) {
        log.info("스터디 삭제 - studyId: {}", studyId);

        // 1. 스터디 조회
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.STUDY_NOT_FOUND));

        int memberCount = study.getMembers().size();

        // 2. 삭제 (내부에 멤버 존재해도 삭제)
        studyRepository.delete(study);

        log.info("✅ 스터디 삭제 완료 - studyId: {}, 함께 삭제된 멤버: {}명", studyId, memberCount);
    }

    /**
     * 스터디에 추가 가능한 멤버 목록 조회
     */
    public List<AvailableMemberResponse> getAvailableMembers(Long studyId) {
        log.info("스터디 추가 가능 멤버 조회 - studyId: {}", studyId);

        // 1. 스터디 존재 확인
        if (!studyRepository.existsById(studyId)) {
            throw new AdminException(AdminErrorCode.STUDY_NOT_FOUND);
        }

        // 2. 이미 참여 중인 멤버 ID 목록
        Set<Long> existingMemberIds = studyMemberRepository
                .findAllByStudyId(studyId)
                .stream()
                .map(StudyMember::getMemberId)
                .collect(Collectors.toSet());

        // 3. 전체 ACTIVE 멤버 조회
        List<Member> allMembers = memberRepository.findAllByStatus(MemberStatus.ACTIVE);

        // 4. 각 멤버가 이미 참여 중인지 표시
        List<AvailableMemberResponse> response = allMembers.stream()
                .map(member -> AvailableMemberResponse.of(
                        member,
                        existingMemberIds.contains(member.getId())
                ))
                .collect(Collectors.toList());

        log.info("✅ 스터디 추가 가능 멤버 조회 완료 - 전체: {}명, 이미 참여: {}명",
                allMembers.size(), existingMemberIds.size());

        return response;
    }

    /**
     * ✅ 스터디에 멤버 추가
     */
    @Transactional
    public void addMembersToStudy(AddStudyMemberRequest request) {
        log.info("스터디에 멤버 추가 - studyId: {}, memberIds: {}",
                request.studyId(), request.memberIds());

        // 1. 스터디 조회
        Study study = studyRepository.findById(request.studyId())
                .orElseThrow(() -> new AdminException(AdminErrorCode.STUDY_NOT_FOUND));

        // 2. 이미 참여 중인 멤버 확인
        Set<Long> existingMemberIds = studyMemberRepository
                .findAllByStudyId(request.studyId())
                .stream()
                .map(StudyMember::getMemberId)
                .collect(Collectors.toSet());

        // 3. 추가할 멤버 필터링 (이미 참여 중이지 않은 멤버만)
        List<Long> membersToAdd = request.memberIds().stream()
                .filter(memberId -> !existingMemberIds.contains(memberId))
                .toList();

        if (membersToAdd.isEmpty()) {
            log.warn("추가할 새로운 멤버가 없음 - studyId: {}", request.studyId());
            return;
        }

        // 4. 멤버 존재 확인
        for (Long memberId : membersToAdd) {
            if (!memberRepository.existsById(memberId)) {
                log.warn("존재하지 않는 멤버 - memberId: {}", memberId);
                throw new AdminException(AdminErrorCode.MEMBER_NOT_FOUND);
            }
        }

        // 5. 멤버 추가 (GUEST 역할로)
        for (Long memberId : membersToAdd) {
            study.addGuest(memberId);
        }

        studyRepository.save(study);

        log.info("✅ 스터디에 멤버 추가 완료 - studyId: {}, 추가된 멤버: {}명",
                request.studyId(), membersToAdd.size());
    }

    /**
     * 전체 스터디 수 조회
     */
    public long getStudyCount() {
        return studyRepository.count();
    }

    /**
     * 중복되지 않는 초대 코드 생성
     */
    private InviteCode generateUniqueInviteCode() {
        InviteCode inviteCode;
        do {
            inviteCode = InviteCode.generate();
        } while (studyRepository.existsByInviteCodeValue(inviteCode.getValue()));

        return inviteCode;
    }
}
