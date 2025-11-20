package econovation.moongtaengi.member.api;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import econovation.moongtaengi.member.application.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
@MockitoBean(types = JpaMetamodelMappingContext.class)
public class MemberControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private MemberService memberService;

    @Test
    @DisplayName("사용 가능한 닉네임은 isAvaiable = true를 반환한다.")
    void 닉네임_체크_성공() throws Exception {
        String validName = "뭉탱이";

        mvc.perform(get("/api/members/check-nickname")
                    .param("nickname", validName)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(validName))
                .andExpect(jsonPath("$.isAvailable").value(true));
    }

    @Test
    @DisplayName("중복되거나 비속어인 닉네임은 isAvailable=false와 메시지를 반환한다")
    void 닉네임_체크_실패() throws Exception {
        String invalidNickname = "개똥이";

        doThrow(new IllegalArgumentException("비속어가 포함되어 있습니다."))
                .when(memberService).checkNicknameAvailability(invalidNickname);

        mvc.perform(get("/api/members/check-nickname")
                        .param("nickname", invalidNickname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isAvailable").value(false))
                .andExpect(jsonPath("$.message").value("비속어가 포함되어 있습니다."));
    }
}
