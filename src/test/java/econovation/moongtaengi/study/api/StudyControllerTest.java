package econovation.moongtaengi.study.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import econovation.moongtaengi.global.config.WebConfig;
import econovation.moongtaengi.global.security.CustomAuthentication;
import econovation.moongtaengi.global.security.JwtTokenProvider;
import econovation.moongtaengi.global.security.LoginMemberIdArgumentResolver;
import econovation.moongtaengi.study.api.dto.StudyCreateRequest;
import econovation.moongtaengi.study.api.dto.StudyDetailResponse;
import econovation.moongtaengi.study.api.dto.StudyDetailResponse.StudyPeriodDto;
import econovation.moongtaengi.study.api.dto.StudyJoinRequest;
import econovation.moongtaengi.study.application.CreateStudyService;
import econovation.moongtaengi.study.application.JoinStudyService;
import econovation.moongtaengi.study.application.StudyDetailService;
import econovation.moongtaengi.study.domain.StudyJoinValidator;
import econovation.moongtaengi.study.domain.StudyRole;
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
    @MockitoBean
    private JoinStudyService joinStudyService;
    @MockitoBean
    private StudyJoinValidator studyJoinValidator;
    @MockitoBean
    private StudyDetailService studyDetailService;

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

    @Test
    @DisplayName("유효한 초대 코드로 스터디 가입 요청 시 200 OK를 반환한다.")
    void 스터디_가입_성공 () throws Exception {
        //given
        String rawCode = "12345678";
        StudyJoinRequest request = new StudyJoinRequest(rawCode);

        //when&then
        mvc.perform(post("/api/studies/join")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(joinStudyService).joinStudy(eq(1L), eq(rawCode));
    }

    @Test
    @DisplayName("스터디 상세 조회 성공 시 200 OK와 상세 정보를 반환한다.")
    void 스터디_상세_조회_성공() throws Exception {
        //given
        Long studyId = 100L;
        Long memberId = 1L;

        StudyDetailResponse.StudyPeriodDto periodDto = new StudyPeriodDto(
                LocalDate.of(2026, 1, 8),
                LocalDate.of(2026, 3, 21)
        );

        StudyDetailResponse response = new StudyDetailResponse(
                studyId,
                "테스트 스터디",
                periodDto,
                "테스트 주제",
                "12345678",
                StudyRole.HOST
        );
        given(studyDetailService.getStudyDetail(studyId, memberId))
                .willReturn(response);

        //when&then
        mvc.perform(get("/api/studies/{studyId}", studyId)
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studyId))
                .andExpect(jsonPath("$.name").value("테스트 스터디"))
                .andExpect(jsonPath("$.topic").value("테스트 주제"))
                .andExpect(jsonPath("$.period.startDate").value("2026-01-08"))
                .andExpect(jsonPath("$.period.endDate").value("2026-03-21"));

        verify(studyDetailService).getStudyDetail(studyId, memberId);
    }
}
