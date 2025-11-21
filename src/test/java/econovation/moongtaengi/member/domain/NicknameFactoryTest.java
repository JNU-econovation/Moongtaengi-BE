package econovation.moongtaengi.member.domain;



import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NicknameFactoryTest {
    @Mock
    private BadWordValidator badWordValidator;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private NicknameFactory nicknameFactory;

    @Test
    @DisplayName("비속어나 중복이 없으면 닉네임 생성에 성공한다.")
    void 닉네임_생성_성공() {
        String validName = "뭉탱이";

        given(badWordValidator.containsBadWord(validName)).willReturn(false);
        given(memberRepository.existsByNicknameValue(validName)).willReturn(false);

        Nickname nickname = nicknameFactory.createNickname(validName);

        assertThat(nickname.getValue()).isEqualTo(validName);
    }

    @Test
    @DisplayName("비속어가 포함되어 있으면 예외가 발생한다.")
    void 비속어_포함_실패() {
        String badName = "똥쟁이";

        given(badWordValidator.containsBadWord(badName)).willReturn(true);

        assertThatThrownBy(() -> nicknameFactory.createNickname(badName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비속어가 포함되어 있습니다.");
    }

    @Test
    @DisplayName("중복된 닉네임이 있으면 예외가 발생한다.")
    void 닉네임_중복_실패() {
        String duplicateName = "중복탱이";

        given(badWordValidator.containsBadWord(duplicateName)).willReturn(false);
        given(memberRepository.existsByNicknameValue(duplicateName)).willReturn(true);

        assertThatThrownBy(() -> nicknameFactory.createNickname(duplicateName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 사용 중인 닉네임입니다.");
    }
}
