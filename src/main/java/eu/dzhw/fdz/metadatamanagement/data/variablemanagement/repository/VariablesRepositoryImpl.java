package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repository;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * This class implements the interface of the custom variable documents repository. This class will
 * be used for the implementation of the repository beans.
 * 
 * @author Daniel Katzberg
 *
 */
public class VariablesRepositoryImpl implements VariablesRepositoryCustom {

  @Autowired
  private ElasticsearchTemplate elasticsearchTemplate;

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repository.VariablesRepositoryCustom#
   * searchAllFields(java.lang.String, org.springframework.data.domain.Pageable)
   */
  @Override
  public Page<VariableDocument> searchAllFields(String query, Pageable pageable) {
    SearchQuery searchQuery = new NativeSearchQueryBuilder()
        .withQuery(
            QueryBuilders.matchQuery("_all", query).fuzziness(Fuzziness.fromSimilarity(0.1f)))
        .withPageable(pageable).build();

    return this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class);
  }

}
