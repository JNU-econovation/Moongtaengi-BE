package econovation.moongtaengi;

import econovation.moongtaengi.global.security.config.JwtConfig;
import econovation.moongtaengi.member.infrastructure.oauth.config.KakaoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(
		{KakaoConfig.class, JwtConfig.class})
public class MoongtaengiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoongtaengiApplication.class, args);
	}

}
