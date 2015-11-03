package eu.dzhw.fdz.metadatamanagement.repository.search;

import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Variable entity.
 */
public interface VariableSearchRepository extends ElasticsearchRepository<Variable, Long> {
}
