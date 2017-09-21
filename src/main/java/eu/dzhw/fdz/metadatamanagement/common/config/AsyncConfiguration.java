package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Configuration for {@link Async} and {@link Scheduled} tasks. *
 */
@Configuration
@Profile("!" + Constants.SPRING_PROFILE_UNITTEST)
@EnableAsync
@EnableScheduling
public class AsyncConfiguration {


}
