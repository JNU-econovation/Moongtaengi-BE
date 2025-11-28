package econovation.moongtaengi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"jwt.secret=TestKeyForCIBuildMustBeOver32BytesLongSoHereIsSomeRandomStringToMakeItWork12345"
})
class MoongtaengiApplicationTests {

	@Test
	void contextLoads() {
	}

}
