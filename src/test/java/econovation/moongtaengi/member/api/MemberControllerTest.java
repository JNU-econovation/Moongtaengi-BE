package econovation.moongtaengi.member.api;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import econovation.moongtaengi.global.security.JwtAuthenticationEntryPoint;
import econovation.moongtaengi.global.security.JwtTokenProvider;
import econovation.moongtaengi.global.security.config.SecurityConfig;
import econovation.moongtaengi.member.application.MemberService;
import econovation.moongtaengi.member.domain.MemberErrorCode;
import econovation.moongtaengi.member.domain.NicknameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
@Import(SecurityConfig.class)
@MockitoBean(types = JpaMetamodelMappingContext.class)
public class MemberControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    @DisplayName("사용 가능한 닉네임은 200 OK와 함께 isAvaiable = true를 반환한다.")
    void 닉네임_체크_성공() throws Exception {
        String validName = "뭉탱이1";

        mvc.perform(get("/api/members/check-nickname")
                    .param("nickname", validName)
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(validName))
                .andExpect(jsonPath("$.isAvailable").value(true));
    }

    @Test
    @DisplayName("비속어인 닉네임은 200 OK와 함께 isAvailable=false와 에러코드(NICK_005)를 반환한다.")
    void 닉네임_체크_성공_비속어_포함() throws Exception {
        String invalidNickname = "개똥이1";

        doThrow(new NicknameException(MemberErrorCode.NICKNAME_BAD_WORD))
                .when(memberService).checkNicknameAvailability(invalidNickname);

        mvc.perform(get("/api/members/check-nickname")
                        .param("nickname", invalidNickname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("NICK_005"))
                .andExpect(jsonPath("$.message").value("비속어가 포함되어 있습니다."));
    }

    @Test
    @DisplayName("이미 존재하는 닉네임은 200 OK와 함께 에러 코드(NICK_002)를 반환한다")
    void 닉네임_체크_성공_중복() throws Exception {
        String duplicateName = "아임중복";

        doThrow(new NicknameException(MemberErrorCode.NICKNAME_DUPLICATED))
                .when(memberService).checkNicknameAvailability(duplicateName);

        mvc.perform(get("/api/members/check-nickname")
                        .param("nickname", duplicateName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isAvailable").value(false))
                .andExpect(jsonPath("$.code").value("NICK_004"))
                .andExpect(jsonPath("$.message").value("이미 사용 중인 닉네임입니다."));
    }
}
