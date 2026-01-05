package econovation.moongtaengi.study.infra.gemini;

import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import java.util.HashMap;
import java.util.Map;

public class ProcessSchema {

    /**
     * 전체 응답 Schema
     */
    public static Schema getResponseSchema() {
        Map<String, Schema> properties = new HashMap<>();
        properties.put("processes", getProcessesSchema());

        return Schema.builder()
                .type(Type.Known.OBJECT)
                .description("학습 프로세스 목록")
                .properties(properties)
                .required("processes")
                .build();
    }

    /**
     * processes 배열 Schema
     */
    private static Schema getProcessesSchema() {
        return Schema.builder()
                .type(Type.Known.ARRAY)
                .description("프로세스 배열")
                .items(getProcessInfoSchema())
                .build();
    }

    /**
     * 개별 프로세스 Schema
     */
    private static Schema getProcessInfoSchema() {
        Map<String, Schema> properties = new HashMap<>();

        properties.put("order", Schema.builder()
                .type(Type.Known.INTEGER)
                .description("프로세스 순서 (1, 2, 3, ...)")
                .build());

        properties.put("title", Schema.builder()
                .type(Type.Known.STRING)
                .description("프로세스 제목")
                .build());

        properties.put("durationDays", Schema.builder()
                .type(Type.Known.INTEGER)
                .description("프로세스 기간 (일 단위)")
                .build());

        properties.put("assignmentDescription", Schema.builder()
                .type(Type.Known.STRING)
                .description("과제 한줄 설명 (마크다운 형식)")
                .build());

        return Schema.builder()
                .type(Type.Known.OBJECT)
                .description("개별 프로세스 정보")
                .properties(properties)
                .required("order", "title", "durationDays", "topic", "assignmentDescription")
                .build();
    }
}
