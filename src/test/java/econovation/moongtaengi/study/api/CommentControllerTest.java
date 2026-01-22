package econovation.moongtaengi.study.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import econovation.moongtaengi.global.config.WebConfig;
import econovation.moongtaengi.global.security.CustomAuthentication;
import econovation.moongtaengi.global.security.LoginMemberIdArgumentResolver;
import econovation.moongtaengi.study.api.dto.CommentCreateRequest;
import econovation.moongtaengi.study.api.dto.CommentUpdateRequest;
import econovation.moongtaengi.study.application.comment.CommentQueryService;
import econovation.moongtaengi.study.application.comment.CommentSummary;
import econovation.moongtaengi.study.application.comment.CreateCommentCommand;
import econovation.moongtaengi.study.application.comment.CreateCommentService;
import econovation.moongtaengi.study.application.comment.UpdateCommentCommand;
import econovation.moongtaengi.study.application.comment.UpdateCommentService;
import java.time.LocalDateTime;
import java.util.List;
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

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({WebConfig.class, LoginMemberIdArgumentResolver.class})
@MockitoBean(types = JpaMetamodelMappingContext.class)
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateCommentService createCommentService;

    @MockitoBean
    private UpdateCommentService updateCommentService;

    @MockitoBean
    private CommentQueryService commentQueryService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthentication(1L));
    }

    @Test
    @DisplayName("댓글 생성 요청 시 201 Created와 Location 헤더를 반환한다.")
    void 댓글_생성_성공() throws Exception {
        Long submissionId = 100L;
        Long createdCommentId = 10L;
        String content = "테스트 댓글";

        CommentCreateRequest request = new CommentCreateRequest(content);
        CreateCommentCommand command = request.toCommand(1L, submissionId);

        given(createCommentService.createComment(any(CreateCommentCommand.class)))
                .willReturn(createdCommentId);

        //when&then
        mockMvc.perform(post("/api/submissions/{submissionId}/comments", submissionId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/comments/" + createdCommentId));

        verify(createCommentService).createComment(command);
    }
    @Test
    @DisplayName("댓글 수정 요청 시 서비스를 호출하고 200 OK를 반환한다.")
    void 댓글_수정_성공() throws Exception {
        //given
        Long commentId = 1L;
        Long memberId = 1L;
        String newContent = "수정된 댓글 내용입니다.";

        CommentUpdateRequest request = new CommentUpdateRequest(newContent);

        UpdateCommentCommand expectedCommand = new UpdateCommentCommand(commentId, memberId, newContent);

        //when&then
        mockMvc.perform(put("/api/comments/{commentId}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());

        verify(updateCommentService).updateComment(expectedCommand);
    }

    @Test
    @DisplayName("제출물 댓글 목록 조회 요청 시 200 OK와 댓글 리스트를 반환한다.")
    void 댓글_목록_조회_성공() throws Exception {
        //given
        Long submissionId = 100L;
        Long loginMemberId = 1L;

        CommentSummary summary = new CommentSummary(
                10L,
                "조회된 댓글 내용",
                LocalDateTime.now(),
                1L,
                "지환",
                "https://icon.url",
                true
        );

        given(commentQueryService.getComments(submissionId, loginMemberId))
                .willReturn(List.of(summary));

        //when&then
        mockMvc.perform(get("/api/submissions/{submissionId}/comments", submissionId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("조회된 댓글 내용"))
                .andExpect(jsonPath("$[0].nickname").value("지환"))
                .andExpect(jsonPath("$[0].isMyComment").value(true));

        verify(commentQueryService).getComments(submissionId, loginMemberId);
    }
}
