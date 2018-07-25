package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;

import eu.dzhw.fdz.metadatamanagement.common.repository.caching.CacheAspect;
import eu.dzhw.fdz.metadatamanagement.common.repository.caching.RequestScopeCache;

/**
 * Enable AspectJ and register common aspects which are relevant for all
 * business components.
 * 
 * @author René Reitmann
 */
@Configuration
@EnableAspectJAutoProxy
public class CommonAspectsConfiguration {
  
  @Bean
  @Profile("!" + Constants.SPRING_PROFILE_UNITTEST)
  public CacheAspect cacheAspect(RequestScopeCache requestScopeCache) {
    return new CacheAspect(requestScopeCache);
  }
}
