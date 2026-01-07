package econovation.moongtaengi.study.api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProcessStatus {
    NOT_STARTED("예정", "아직 시작하지 않은 프로세스"),
    IN_PROGRESS("진행중", "현재 진행 중인 프로세스"),
    COMPLETED("완료", "완료된 프로세스");

    private final String description;
    private final String detail;
}
