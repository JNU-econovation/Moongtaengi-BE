package econovation.moongtaengi.study.api;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import econovation.moongtaengi.global.config.WebConfig;
import econovation.moongtaengi.global.security.CustomAuthentication;
import econovation.moongtaengi.global.security.LoginMemberIdArgumentResolver;
import econovation.moongtaengi.study.api.dto.ReactionToggleRequest;
import econovation.moongtaengi.study.application.reaction.ToggleReactionService;
import econovation.moongtaengi.study.domain.reaction.EmojiType;
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

@WebMvcTest(ReactionController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({WebConfig.class, LoginMemberIdArgumentResolver.class})
@MockitoBean(types = JpaMetamodelMappingContext.class)
public class ReactionControllerTest {

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthentication(1L));
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ToggleReactionService toggleReactionService;

    @Test
    @DisplayName("감정표현 토글 요청 시 서비스가 올바르게 호출된다.")
    void 감정표현_토글_요청_성공() throws Exception {
        //given
        Long submissionId = 10L;
        EmojiType emojiType = EmojiType.HEART;
        ReactionToggleRequest request = new ReactionToggleRequest(emojiType);

        //when&then
        mvc.perform(post("/api/submissions/{submissionId}/reactions", submissionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(toggleReactionService).toggleReaction(submissionId, 1L, emojiType);
    }
}
