package eu.dzhw.fdz.metadatamanagement.config.elasticsearch;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import eu.dzhw.fdz.metadatamanagement.data.common.ElasticSearchPopulator;
import eu.dzhw.fdz.metadatamanagement.data.common.PopulatorUtils;

/**
 * This is the configuration file of the data populator.
 * @author Daniel Katzberg
 *
 */
@Configuration
public class DataPopulatorConfig {

  @Bean
  public ElasticSearchPopulator repositoryPopulator(ApplicationContext applicationContext,
      PopulatorUtils populatorUtils, ElasticsearchTemplate elasticsearchTemplate) {
    return new ElasticSearchPopulator(applicationContext, populatorUtils, elasticsearchTemplate);
  }

  @Bean
  public PopulatorUtils populatorUtils(final ResourceLoader resourceLoader) {
    return new PopulatorUtils(resourceLoader);
  }
}
