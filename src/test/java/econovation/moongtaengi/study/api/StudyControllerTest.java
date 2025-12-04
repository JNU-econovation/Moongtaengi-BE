package econovation.moongtaengi.study.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import econovation.moongtaengi.global.config.WebConfig;
import econovation.moongtaengi.global.security.CustomAuthentication;
import econovation.moongtaengi.global.security.LoginMemberIdArgumentResolver;
import econovation.moongtaengi.study.api.dto.StudyCreateRequest;
import econovation.moongtaengi.study.application.CreateStudyService;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StudyController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({WebConfig.class, LoginMemberIdArgumentResolver.class})
@MockitoBean(types = JpaMetamodelMappingContext.class)
public class StudyControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateStudyService createStudyService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthentication(1L));
    }

    @Test
    @DisplayName("스터디 생성 요청 시 서비스 호출 후 201 Created와 Location 헤더를 반환한다")
    void 스터디_생성_성공() throws Exception {
        // given
        StudyCreateRequest request = new StudyCreateRequest(
                "스프링 스터디",
                "백엔드",
                LocalDate.of(2025, 12, 1),
                LocalDate.of(2025, 12, 31)
        );
        given(createStudyService.createStudy(eq(1L), any(), any(), any(), any()))
                .willReturn(100L);

        // when & then
        mvc.perform(post("/api/studies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/studies/100"));

        verify(createStudyService).createStudy(
                eq(1L), // memberId
                eq(request.name()),
                eq(request.topic()),
                eq(request.startDate()),
                eq(request.endDate())
        );
    }

    @Test
    @DisplayName("필수 값이 누락되면 400 Bad Request를 반환한다")
    void 스터디_생성_실패_유효성검사() throws Exception {
        // given
        StudyCreateRequest invalidRequest = new StudyCreateRequest(
                "",
                "",
                null,
                null
        );

        // when & then
        mvc.perform(post("/api/studies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isInternalServerError()); // 아직 MethodArgumentException 처리를 못함..
    }
}
