package econovation.moongtaengi.study.api;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import econovation.moongtaengi.global.config.WebConfig;
import econovation.moongtaengi.global.security.CustomAuthentication;
import econovation.moongtaengi.global.security.LoginMemberIdArgumentResolver;
import econovation.moongtaengi.study.api.dto.StudySummaryResponse;
import econovation.moongtaengi.study.application.StudyQueryService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MyStudyController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({WebConfig.class, LoginMemberIdArgumentResolver.class})
@MockitoBean(types = JpaMetamodelMappingContext.class)
public class MyStudyControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private StudyQueryService studyQueryService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthentication(1L));
    }

    @Test
    @DisplayName("내가 관리하는 스터디 목록을 조회한다.")
    void 내가_관리하는_스터디_조회_성공() throws Exception {
        //given
        List<StudySummaryResponse> response = List.of(
                new StudySummaryResponse(100L, "관리 스터디")
        );
        given(studyQueryService.getManagedStudies(anyLong()))
                .willReturn(response);

        //when&then
        mvc.perform(get("/api/studies/me/managed"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].studyId").value(100L))
                .andExpect(jsonPath("$[0].studyName").value("관리 스터디"));

        verify(studyQueryService).getManagedStudies(1L);
    }

    @Test
    @DisplayName("내가 참여 중인 스터디 목록을 조회한다.")
    void 내가_참여중인_스터디_조회_성공() throws Exception {
        //given
        List<StudySummaryResponse> response = List.of(
                new StudySummaryResponse(100L, "참여 스터디")
        );
        given(studyQueryService.getJoinedStudies(anyLong()))
                .willReturn(response);

        //when&then
        mvc.perform(get("/api/studies/me/joined"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].studyId").value(100L))
                .andExpect(jsonPath("$[0].studyName").value("참여 스터디"));
        verify(studyQueryService).getJoinedStudies(1L);
    }
}
