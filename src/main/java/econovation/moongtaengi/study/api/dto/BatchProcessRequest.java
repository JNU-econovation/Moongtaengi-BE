package econovation.moongtaengi.study.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

/**
 * 프로세스 일괄 저장 요청 DTO
 */
public record BatchProcessRequest(
        @NotNull(message = "프로세스 목록은 필수입니다")
        @Size(min = 1, message = "최소 1개 이상의 프로세스가 필요합니다")
        @Valid
        List<ProcessItem> processes
) {
    /**
     * 개별 프로세스 정보
     */
    public record ProcessItem(
            Long id,  // null이면 신규, null 아니면 기존 값 수정

            @NotBlank(message = "프로세스 제목은 필수입니다")
            @Size(max = 100, message = "프로세스 제목은 최대 100자입니다")
            String title,

            @NotNull(message = "시작일은 필수입니다")
            LocalDate startDate,

            @NotNull(message = "종료일은 필수입니다")
            LocalDate endDate,

            @Size(max = 50, message = "메모는 최대 50자입니다")
            String memo,

            // @NotBlank(message = "과제 설명은 필수입니다")
            // 🔥 과제 설명을 어디에 입력 할지 미정
            String assignmentDescription
    ) {}
}
