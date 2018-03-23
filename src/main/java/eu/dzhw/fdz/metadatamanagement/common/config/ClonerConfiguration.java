package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rits.cloning.Cloner;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;

/**
 * Configure (deep) cloning of java objects.
 * 
 * @author René Reitmann
 */
@Configuration
public class ClonerConfiguration {

  /**
   * Configure the cloner not to clone projections.
   * 
   * @return The cloner which can deep clone java objects.
   */
  @Bean
  public Cloner cloner() {
    Cloner cloner = new Cloner();
    cloner.dontCloneInstanceOf(IdAndVersionProjection.class);
    return cloner;
  }
}
