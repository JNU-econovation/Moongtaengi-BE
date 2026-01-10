package econovation.moongtaengi.admin.application;

import econovation.moongtaengi.admin.api.dto.AdminStudyResponse;
import econovation.moongtaengi.admin.api.dto.CreateStudyRequest;
import econovation.moongtaengi.admin.domain.AdminErrorCode;
import econovation.moongtaengi.admin.domain.AdminException;
import econovation.moongtaengi.member.domain.MemberRepository;
import econovation.moongtaengi.study.domain.*;
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
