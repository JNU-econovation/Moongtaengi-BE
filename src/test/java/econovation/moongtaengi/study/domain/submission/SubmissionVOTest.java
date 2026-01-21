package econovation.moongtaengi.study.domain.submission;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class SubmissionVOTest {
    @Nested
    @DisplayName("제출 내용")
    class SubmissionContentTest {
        @Test
        @DisplayName("제출 내용을 정상적으로 생성한다")
        void 제출_내용_생성_성공 () {
            //given
            String value = "과제 수행 결과";

            //when
            SubmissionContent content = new SubmissionContent(value);

            //then
            assertThat(content.getValue()).isEqualTo(value);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("제출 내용이 없으면 예외가 발생한다")
        void 제출_내용_비어있음_실패(String invalidValue) {
            //when&then
            assertThatThrownBy(() -> new SubmissionContent(invalidValue))
                    .isInstanceOf(SubmissionException.class)
                    .extracting("errorCode")
                    .isEqualTo(SubmissionErrorCode.INVALID_SUBMISSION_INFO);
        }

        @Test
        @DisplayName("제출 내용이 제한 길이(50,000자)를 초과하면 예외가 발생한다")
        void 제출_내용_길이초과_실패() {
            //given
            int maxLength = SubmissionContent.MAX_LENGTH;
            String longValue = "a".repeat(maxLength + 1);

            //when&then
            assertThatThrownBy(() -> new SubmissionContent(longValue))
                    .isInstanceOf(SubmissionException.class)
                    .extracting("errorCode")
                    .isEqualTo(SubmissionErrorCode.CONTENT_TOO_LONG);
        }
    }

    @Nested
    @DisplayName("첨부파일")
    class SubmissionAttachmentTest {
        @Test
        @DisplayName("정상적인 URL로 첨부파일을 생성한다")
        void 첨부파일_생성_성공() {
            //given
            String fileName = "파일명";
            String validUrl = "https://example.com/file.pdf";

            //when
            SubmissionAttachment attachment = SubmissionAttachment.of(fileName, validUrl);

            //then
            assertThat(attachment.getUrl()).isEqualTo(validUrl);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("URL이 없으면 예외가 발생한다")
        void 첨부파일_URL_없음_실패(String invalidUrl) {
            //when&then
            assertThatThrownBy(() -> SubmissionAttachment.of("파일명", invalidUrl))
                    .isInstanceOf(SubmissionException.class)
                    .extracting("errorCode")
                    .isEqualTo(SubmissionErrorCode.INVALID_ATTACHMENT_URL);
        }

        @ParameterizedTest
        @ValueSource(strings = {"ftp://files.com", "not-url", "www.naver.com"})
        @DisplayName("URL 형식이 올바르지 않으면 예외가 발생한다")
        void 첨부파일_URL_형식_실패(String invalidUrl) {
            //when&then
            assertThatThrownBy(() -> SubmissionAttachment.of("파일명", invalidUrl))
                    .isInstanceOf(SubmissionException.class)
                    .extracting("errorCode")
                    .isEqualTo(SubmissionErrorCode.INVALID_ATTACHMENT_URL);
        }
    }
}

