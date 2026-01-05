package econovation.moongtaengi.study.application;

import econovation.moongtaengi.study.api.dto.BatchProcessRequest;
import econovation.moongtaengi.study.api.dto.GeminiProcessResponse;
import econovation.moongtaengi.study.api.dto.ProcessResponse;
import econovation.moongtaengi.study.domain.Study;
import econovation.moongtaengi.study.domain.StudyErrorCode;
import econovation.moongtaengi.study.domain.StudyException;
import econovation.moongtaengi.study.domain.StudyRepository;
import econovation.moongtaengi.study.domain.process.StudyProcess;
import econovation.moongtaengi.study.domain.process.StudyProcessRepository;
import econovation.moongtaengi.study.infra.gemini.GeminiClient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 프로세스 관련 비즈니스 로직
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProcessService {
    private final GeminiClient geminiClient;
    private final StudyRepository studyRepository;
    private final StudyProcessRepository studyProcessRepository;

    /**
     * 프로세스 생성 (Gemini 호출)
     *
     * @param studyId 스터디 ID
     * @param memberId 요청한 회원 ID
     * @param additionalDescription 추가 설명
     */
    @Transactional
    public void generateProcesses(Long studyId, Long memberId, String additionalDescription) {
        // 1. Study 조회
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() ->new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        log.info("스터디 조회 완료 - studyId: {}, topic: {}", studyId, study.getTopic().getValue());

        // 2. 호스트 권한 확인
        try {
            study.validateHost(memberId);
            log.info("호스트 권한 확인 완료 - studyId: {}, memberId: {}", studyId, memberId);
        } catch (StudyException e) {
            log.warn("호스트 권한 없음 - studyId: {}, memberId: {}, errorCode: {}",
                    studyId, memberId, e.getErrorCode());
            throw e;
        }

        // 3. 기존 프로세스 삭제 (있다면)
        if (studyProcessRepository.existsByStudyId(studyId)) {
            studyProcessRepository.deleteByStudyId(studyId);
            log.info("기존 프로세스 삭제 완료 - studyId: {}", studyId);
        }

        // 4. Gemini API 호출
        log.info("Gemini API 호출 시작 - studyId: {}", studyId);

        GeminiProcessResponse geminiResponse = geminiClient.generateProcesses(
                study.getTopic().getValue(),
                study.getPeriod().getStartDate(),
                study.getPeriod().getEndDate(),
                additionalDescription
        );

        log.info("Gemini API 호출 성공 - 프로세스 개수: {}", geminiResponse.processes().size());

        // 5. StudyProcess 엔티티 생성
        List<StudyProcess> processes = createProcesses(
                studyId,
                geminiResponse,
                study.getPeriod().getStartDate()
        );

        // 6. 저장
        studyProcessRepository.saveAll(processes);

        log.info("✅ 프로세스 생성 완료 - studyId: {}, 개수: {}", studyId, processes.size());
    }


    /**
     * Gemini 응답 → StudyProcess 엔티티 변환
     *
     * 날짜 계산 로직:
     * - 첫 프로세스: 스터디 시작일부터
     * - 이후 프로세스: 이전 프로세스 종료일 + 1일
     */
    private List<StudyProcess> createProcesses(
            Long studyId,
            GeminiProcessResponse geminiResponse,
            LocalDate studyStartDate
    ) {
        List<StudyProcess> processes = new ArrayList<>();
        LocalDate currentStartDate = studyStartDate;

        for (var processInfo : geminiResponse.processes()) {
            // 시작일
            LocalDate startDate = currentStartDate;

            // 종료일 = 시작일 + (기간 - 1)
            // 예: 5일 프로세스 → 1일~5일 (종료일 = 시작일 + 4)
            LocalDate endDate = currentStartDate.plusDays(processInfo.durationDays() - 1);

            StudyProcess process = StudyProcess.create(
                    studyId,
                    processInfo.order(),
                    processInfo.title(),
                    startDate,
                    endDate,
                    processInfo.assignmentDescription()
            );

            processes.add(process);

            log.debug("프로세스 생성 - order: {}, 기간: {} ~ {} ({}일)",
                    processInfo.order(), startDate, endDate, processInfo.durationDays());

            // 다음 프로세스 시작일 = 현재 프로세스 종료일 + 1
            currentStartDate = endDate.plusDays(1);
        }

        return processes;
    }

    /**
     * 프로세스 전체 목록 조회
     *
     * @param studyId 스터디 ID
     * @param memberId 요청한 회원 ID
     * @return 프로세스 목록
     */
    public List<ProcessResponse> getProcesses(Long studyId, Long memberId) {
        // 1. Study 존재 & 멤버 확인
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        study.validateMember(memberId);

        log.info("프로세스 목록 조회 권한 확인 완료 - studyId: {}, memberId: {}", studyId, memberId);

        // 2. 프로세스 조회
        List<StudyProcess> processes = studyProcessRepository
                .findByStudyIdOrderByProcessOrder(studyId);

        // 3. DTO 변환
        return processes.stream()
                .map(ProcessResponse::from)
                .toList();
    }

    /**
     * 프로세스 단건 조회
     *
     * @param studyId 스터디 ID
     * @param processId 프로세스 ID
     * @param memberId 요청한 회원 ID
     * @return 프로세스 정보
     */
    public ProcessResponse getProcess(Long studyId, Long processId, Long memberId) {
        // 1. Study 존재 & 멤버 확인
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        study.validateMember(memberId);

        log.info("프로세스 단건 조회 권한 확인 완료 - studyId: {}, memberId: {}", studyId, memberId);

        // 2. Process 조회
        StudyProcess process = studyProcessRepository.findById(processId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.PROCESS_NOT_FOUND));

        // 3. 해당 Study의 프로세스인지 확인
        if (!process.getStudyId().equals(studyId)) {
            throw new StudyException(StudyErrorCode.PROCESS_NOT_FOUND);
        }

        // 4. DTO 변환
        return ProcessResponse.from(process);
    }

    /**
     * 프로세스 일괄 저장 (신규 추가 + 기존 수정)
     *
     * - 삭제는 별도 DELETE API로 처리
     * - 배열 순서대로 processOrder 부여 (1, 2, 3, ...)
     *
     * @param studyId 스터디 ID
     * @param memberId 요청한 회원 ID
     * @param request 프로세스 목록 (날짜순 정렬되어야 함)
     */
    @Transactional
    public void batchProcesses(Long studyId, Long memberId, BatchProcessRequest request) {
        // 1. 권한 확인
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        study.validateHost(memberId);

        log.info("일괄 저장 권한 확인 완료 - studyId: {}, memberId: {}", studyId, memberId);

        // 2. 날짜 겹침 검증
        validateProcessDateOverlap(request.processes());

        // 3. 추가/수정 (배열 순서대로 processOrder 부여)
        List<StudyProcess> toSave = new ArrayList<>();
        int order = 1;

        for (var item : request.processes()) {
            StudyProcess process;

            if (item.id() == null) {
                // 신규 프로세스 생성
                process = StudyProcess.create(
                        studyId,
                        order,
                        item.title(),
                        item.startDate(),
                        item.endDate(),
                        item.assignmentDescription() != null ? item.assignmentDescription() : "" // 🔥 임시로 이렇게 구현 추후에 과제 설명란 넣는 방식 결정되면 수정하기
                );

                // 메모 설정 (있으면)
                if (item.memo() != null && !item.memo().isBlank()) {
                    process.updateMemo(item.memo());
                }

                log.debug("신규 프로세스 생성 - order: {}, title: {}", order, item.title());

            } else {
                // 기존 프로세스 수정
                process = studyProcessRepository.findById(item.id())
                        .orElseThrow(() -> new StudyException(StudyErrorCode.PROCESS_NOT_FOUND));

                // 해당 스터디의 프로세스인지 확인
                if (!process.getStudyId().equals(studyId)) {
                    throw new StudyException(StudyErrorCode.PROCESS_NOT_FOUND);
                }

                process.update(
                        order,
                        item.title(),
                        item.startDate(),
                        item.endDate(),
                        item.memo(),
                        item.assignmentDescription()
                );

                log.debug("프로세스 수정 - id: {}, order: {} → {}, title: {}",
                        item.id(), process.getProcessOrder(), order, item.title());
            }

            toSave.add(process);
            order++;
        }

        // 4. 저장
        studyProcessRepository.saveAll(toSave);

        log.info("✅ 프로세스 일괄 저장 완료 - studyId: {}, 저장 개수: {}", studyId, toSave.size());
    }

    /**
     * 프로세스 날짜 겹침 검증
     *
     * 개별 기간 검증(null, startDate <= endDate, 3~90일)은
     * ProcessPeriod 생성자에서 자동으로 수행됨
     */
    private void validateProcessDateOverlap(List<BatchProcessRequest.ProcessItem> processes) {
        for (int i = 0; i < processes.size() - 1; i++) {
            LocalDate currentEnd = processes.get(i).endDate();
            LocalDate nextStart = processes.get(i + 1).startDate();

            if (nextStart.isBefore(currentEnd.plusDays(1))) {
                log.error("프로세스 날짜 겹침 - process{}: {} ~ {}, process{}: {} ~ {}",
                        i + 1, processes.get(i).startDate(), currentEnd,
                        i + 2, nextStart, processes.get(i + 1).endDate());
                throw new StudyException(StudyErrorCode.PROCESS_DATE_OVERLAP);
            }
        }

        log.debug("프로세스 날짜 겹침 검증 완료 - 개수: {}", processes.size());
    }
}
