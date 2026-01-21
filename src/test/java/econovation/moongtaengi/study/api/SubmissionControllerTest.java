package econovation.moongtaengi.study.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
import econovation.moongtaengi.study.api.dto.SubmissionCreateRequest;
import econovation.moongtaengi.study.application.submission.CreateSubmissionCommand;
import econovation.moongtaengi.study.application.submission.CreateSubmissionService;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SubmissionController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({WebConfig.class, LoginMemberIdArgumentResolver.class})
@MockitoBean(types = JpaMetamodelMappingContext.class)
public class SubmissionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateSubmissionService createSubmissionService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthentication(1L));
    }

    @Test
    @DisplayName("과제 제출 요청이 오면 201 Created와 생성된 리소스 위치를 반환한다")
    void 과제_제출_성공() throws Exception {
        //given
        SubmissionCreateRequest request = new SubmissionCreateRequest(
                1L,
                "테스트 제출물",
                "과제.pdf",
                "http://image.url"
        );

        given(createSubmissionService.createSubmission(any(CreateSubmissionCommand.class)))
                .willReturn(1L);

        //when&then
        mvc.perform(post("/api/submissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/submissions/1"));

        ArgumentCaptor<CreateSubmissionCommand> commandCaptor = ArgumentCaptor.forClass(CreateSubmissionCommand.class);
        verify(createSubmissionService).createSubmission(commandCaptor.capture());

        CreateSubmissionCommand passedCommand = commandCaptor.getValue();

        assertThat(passedCommand.submitterId()).isEqualTo(1L);
        assertThat(passedCommand.assignmentId()).isEqualTo(request.assignmentId());
        assertThat(passedCommand.content()).isEqualTo(request.content());
        //assertThat(passedCommand.attachment).hasSize(1);
    }

    @DisplayName("필수값이 누락되거나 유효하지 않은 경우 400 Bad Request를 반환한다")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("invalidRequestProvider")
    void 과제_제출_실패_유효성검사(String description, Long assignmentId, String content) throws Exception {
        //given
        SubmissionCreateRequest badRequest = new SubmissionCreateRequest(
                assignmentId,
                content,
                null,
                null
        );

        //when&then
        mvc.perform(post("/api/submissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    static Stream<Arguments> invalidRequestProvider() {
        return Stream.of(
                Arguments.of("과제 ID 누락", null, "테스트 제출 내용"),
                Arguments.of("내용 누락(null)", 1L, null),
                Arguments.of("내용 누락(빈 문자열)", 1L, ""),
                Arguments.of("내용 누락(공백)", 1L, "   ")
        );
    }
}
