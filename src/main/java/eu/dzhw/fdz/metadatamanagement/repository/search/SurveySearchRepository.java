package eu.dzhw.fdz.metadatamanagement.repository.search;

import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Survey entity.
 */
public interface SurveySearchRepository extends ElasticsearchRepository<Survey, Long> {
}
