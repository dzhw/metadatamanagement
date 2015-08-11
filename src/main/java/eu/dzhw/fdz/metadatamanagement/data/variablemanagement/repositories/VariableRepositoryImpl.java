package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories;

import static org.elasticsearch.index.query.FilterBuilders.boolFilter;
import static org.elasticsearch.index.query.FilterBuilders.termFilter;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder.ZeroTermsQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import eu.dzhw.fdz.metadatamanagement.data.common.QueryAnalyzers;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * This class implements the interface of the custom variable documents repository. This class will
 * be used for the implementation of the repository beans.
 * 
 * @author Daniel Katzberg
 *
 */
public class VariableRepositoryImpl implements VariableRepositoryCustom {

  private ElasticsearchTemplate elasticsearchTemplate;

  @Autowired
  public VariableRepositoryImpl(ElasticsearchTemplate elasticsearchTemplate) {
    this.elasticsearchTemplate = elasticsearchTemplate;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repository.VariablesRepositoryCustom#
   * matchQueryInAllField(java.lang.String, org.springframework.data.domain.Pageable)
   */
  @Override
  public Page<VariableDocument> matchQueryInAllField(String query, Pageable pageable) {
    QueryBuilder queryBuilder = QueryBuilders.matchQuery("_all", query).fuzziness(Fuzziness.AUTO)
        .analyzer(QueryAnalyzers.getAnalyzerForCurrentLocale()).zeroTermsQuery(ZeroTermsQuery.ALL);

    SearchQuery searchQuery =
        new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable).build();

    return this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repository.VariablesRepositoryCustom#
   * matchPhrasePrefixQuery(java.lang.String, org.springframework.data.domain.Pageable)
   */
  @Override
  public Page<VariableDocument> matchPhrasePrefixQuery(String query, Pageable pageable) {

    QueryBuilder queryBuilder = QueryBuilders.matchPhrasePrefixQuery("_all", query)
        .fuzziness(Fuzziness.AUTO).analyzer(QueryAnalyzers.getAnalyzerForCurrentLocale());

    SearchQuery searchQuery =
        new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable).build();

    return this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableRepositoryCustom#
   * matchFilterBySurveyId(java.lang.String, org.springframework.data.domain.Pageable)
   */
  @Override
  public Page<VariableDocument> matchFilterBySurveyId(String query, Pageable pageable) {

    FilterBuilder filter = boolFilter().must(termFilter("surveyId", query));
    
    SearchQuery searchQuery =
        new NativeSearchQueryBuilder().withFilter(filter).withPageable(pageable).build();

    return this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class);
  }
}