package econovation.moongtaengi.study.api;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import econovation.moongtaengi.global.config.WebConfig;
import econovation.moongtaengi.global.security.CustomAuthentication;
import econovation.moongtaengi.global.security.LoginMemberIdArgumentResolver;
import econovation.moongtaengi.study.api.dto.AssignmentCreateRequest;
import econovation.moongtaengi.study.application.assignment.ApproveAssignmentService;
import econovation.moongtaengi.study.application.assignment.AssignmentDetail;
import econovation.moongtaengi.study.application.assignment.AssignmentQueryService;
import econovation.moongtaengi.study.application.assignment.AssignmentSummary;
import econovation.moongtaengi.study.application.assignment.CreateAssignmentCommand;
import econovation.moongtaengi.study.application.assignment.CreateAssignmentService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import econovation.moongtaengi.study.domain.StudyErrorCode;
import econovation.moongtaengi.study.domain.StudyException;
import econovation.moongtaengi.study.domain.assignment.AssignmentErrorCode;
import econovation.moongtaengi.study.domain.assignment.AssignmentException;
import econovation.moongtaengi.study.domain.assignment.AssignmentStatus;
import java.time.LocalDateTime;
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

@WebMvcTest(AssignmentController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({WebConfig.class, LoginMemberIdArgumentResolver.class})
@MockitoBean(types = JpaMetamodelMappingContext.class)
public class AssignmentControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateAssignmentService createAssignmentService;

    @MockitoBean
    private AssignmentQueryService assignmentQueryService;

    @MockitoBean
    private ApproveAssignmentService approveAssignmentService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthentication(1L));
    }

    @Test
    @DisplayName("모든 정보가 포함된 요청이 오면 Command로 변환하여 서비스를 호출하고 201을 반환한다")
    void 과제_생성_성공_날짜포함() throws Exception {
        //given
        Long createdAssignmentId = 1L;
        LocalDateTime deadline = LocalDateTime.now().plusDays(7);

        AssignmentCreateRequest request = new AssignmentCreateRequest(
                99L,
                1L,
                "테스트 과제",
                deadline
        );

        given(createAssignmentService.createAssignment(any(CreateAssignmentCommand.class)))
                .willReturn(createdAssignmentId);

        //when
        mvc.perform(post("/api/assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/assignments/" + createdAssignmentId));

        //then
        ArgumentCaptor<CreateAssignmentCommand> captor = ArgumentCaptor.forClass(CreateAssignmentCommand.class);
        verify(createAssignmentService).createAssignment(captor.capture());

        CreateAssignmentCommand command = captor.getValue();

        assertThat(command.processId()).isEqualTo(request.processId());
        assertThat(command.requesterId()).isEqualTo(1L);
        assertThat(command.content()).isEqualTo(request.content());
        assertThat(command.deadline()).isEqualTo(deadline);
    }

    @Test
    @DisplayName("마감일이 없이(null) 요청이 와도 400 에러 없이 서비스에 null 상태로 전달된다")
    void 과제_생성_성공_마감일_미입력() throws Exception {
        //given
        Long createdAssignmentId = 2L;

        AssignmentCreateRequest request = new AssignmentCreateRequest(
                99L,
                1L,
                "테스트 과제",
                null
        );

        given(createAssignmentService.createAssignment(any(CreateAssignmentCommand.class)))
                .willReturn(createdAssignmentId);

        //when&then
        mvc.perform(post("/api/assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());

        ArgumentCaptor<CreateAssignmentCommand> captor = ArgumentCaptor.forClass(CreateAssignmentCommand.class);
        verify(createAssignmentService).createAssignment(captor.capture());

        CreateAssignmentCommand command = captor.getValue();

        assertThat(command.deadline()).isNull();
    }

    @DisplayName("필수값이 누락된 경우 400 Bad Request를 반환한다")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("invalidRequestProvider")
    void 과제_생성_실패_유효성검사(String description, Long processId, Long assigneeId, String content) throws Exception {
        //given
        AssignmentCreateRequest badRequest = new AssignmentCreateRequest(
                processId,
                assigneeId,
                content,
                null
        );

        //when&then
        mvc.perform(post("/api/assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    static Stream<Arguments> invalidRequestProvider() {
        return Stream.of(
                Arguments.of("프로세스 ID 누락", null, 1L, "테스트 과제"),
                Arguments.of("담당자 ID 누락", 99L, null, "테스트 과제"),
                Arguments.of("내용 누락(null)", 99L, 1L, null),
                Arguments.of("내용 누락(빈 문자열)", 99L, 1L, ""),
                Arguments.of("내용 누락(공백)", 99L, 1L, "   ")
        );
    }

    @Test
    @DisplayName("과제 목록 조회 요청 시, 서비스 호출 후 과제 요약 리스트를 반환한다")
    void 과제_목록_조회_성공() throws Exception {
        //given
        Long memberId = 1L;
        Long processId = 100L;

        List<AssignmentSummary> summaries = List.of(
                new AssignmentSummary(
                        10L,
                        1L,
                        memberId,
                        "알고리즘 과제",
                        "지환", AssignmentStatus.SUBMITTED,
                        false,
                        "http://file.url"),
                new AssignmentSummary(11L,
                        null,
                        2L,
                        "JPA 과제",
                        "철수",
                        AssignmentStatus.WAITING,
                        true,
                        null)
        );

        given(assignmentQueryService.getAssignmentSummaries(memberId, processId))
                .willReturn(summaries);

        //when&then
        mvc.perform(get("/api/assignments")
                        .param("processId", String.valueOf(processId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nickname").value("지환"))
                .andExpect(jsonPath("$[0].status").value("SUBMITTED"))
                .andExpect(jsonPath("$[0].isLate").value(false))
                .andExpect(jsonPath("$[1].nickname").value("철수"))
                .andExpect(jsonPath("$[1].status").value("WAITING"))
                .andExpect(jsonPath("$[1].isLate").value(true));

        verify(assignmentQueryService).getAssignmentSummaries(memberId, processId);
    }

    @Test
    @DisplayName("과제 상세 조회 성공 시 200 OK와 함께 상세 정보를 반환한다")
    void 과제_상세_조회_성공() throws Exception {
        //given
        Long loginMemberId = 1L;
        Long assignmentId = 10L;

        AssignmentDetail response = new AssignmentDetail(
                100L,
                "테스트 스터디",
                "테스트 과제",
                "지환",
                "비기너",
                null,
                null,
                true
        );

        given(assignmentQueryService.getAssignmentDetail(loginMemberId, assignmentId))
                .willReturn(response);

        //when&then
        mvc.perform(get("/api/assignments/{assignmentId}", assignmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studyId").value(100L))
                .andExpect(jsonPath("$.studyName").value("테스트 스터디"))
                .andExpect(jsonPath("$.assignmentContent").value("테스트 과제"))
                .andExpect(jsonPath("$.nickname").value("지환"))
                .andExpect(jsonPath("$.memberTitle").value("비기너"))
                .andExpect(jsonPath("$.isOwner").value(true))
                .andExpect(jsonPath("$.submissionId").isEmpty());

        verify(assignmentQueryService).getAssignmentDetail(loginMemberId, assignmentId);
    }

    @Test
    @DisplayName("존재하지 않는 과제 조회 시 404 Not Found를 반환한다")
    void 과제_상세_조회_실패_미존재() throws Exception {
        //given
        Long invalidId = 999L;

        given(assignmentQueryService.getAssignmentDetail(any(), eq(invalidId)))
                .willThrow(new AssignmentException(AssignmentErrorCode.ASSIGNMENT_NOT_FOUND));

        //when&then
        mvc.perform(get("/api/assignments/{assignmentId}", invalidId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("스터디 멤버가 아닌 경우 403 Forbidden을 반환한다")
    void 과제_상세_조회_실패_권한없음() throws Exception {
        //given
        Long assignmentId = 10L;

        given(assignmentQueryService.getAssignmentDetail(any(), eq(assignmentId)))
                .willThrow(new StudyException(StudyErrorCode.NOT_STUDY_MEMBER));

        //when&then
        mvc.perform(get("/api/assignments/{assignmentId}", assignmentId))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("과제 승인 요청 시 서비스를 호출하고 200 OK를 반환한다")
    void 과제_승인_성공() throws Exception {
        //given
        Long assignmentId = 100L;
        Long requesterId = 1L;

        //when&then
        mvc.perform(post("/api/assignments/{assignmentId}/approve", assignmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(approveAssignmentService).approveAssignment(assignmentId, requesterId);
    }
}
