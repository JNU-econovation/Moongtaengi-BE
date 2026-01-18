package econovation.moongtaengi.study.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import econovation.moongtaengi.global.config.WebConfig;
import econovation.moongtaengi.global.security.CustomAuthentication;
import econovation.moongtaengi.global.security.LoginMemberIdArgumentResolver;
import econovation.moongtaengi.study.api.dto.CommentCreateRequest;
import econovation.moongtaengi.study.application.comment.CreateCommentCommand;
import econovation.moongtaengi.study.application.comment.CreateCommentService;
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
}
