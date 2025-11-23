package econovation.moongtaengi.global.config;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class TimeZoneConfig {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        log.info("Timezone set to Asia/Seoul: {}", TimeZone.getDefault().getID());
    }
}
