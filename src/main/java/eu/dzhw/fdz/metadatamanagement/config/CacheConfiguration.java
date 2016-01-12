package eu.dzhw.fdz.metadatamanagement.config;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuration for a jvm wide cache. Currently not used.
 */
@Configuration
@EnableCaching
@AutoConfigureAfter(value = {MetricsConfiguration.class, DatabaseConfiguration.class})
@Profile("!" + Constants.SPRING_PROFILE_FAST)
public class CacheConfiguration {

  private final Logger log = LoggerFactory.getLogger(CacheConfiguration.class);

  private CacheManager cacheManager;

  @PreDestroy
  public void destroy() {
    log.info("Closing Cache Manager");
  }

  /**
   * Create a {@link NoOpCacheManager}.
   * @return the {@link NoOpCacheManager}
   */
  @Bean
  public CacheManager cacheManager() {
    log.debug("No cache");
    cacheManager = new NoOpCacheManager();
    return cacheManager;
  }
}
