package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories;


import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MatchQueryBuilder.ZeroTermsQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class VariableRepositoryImpl implements VariableRepositoryCustom {

  @Value("${search.minimum-should-match-ngrams}")
  private String minimumShouldMatch;

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
    QueryBuilder queryBuilder = matchQuery("_all", query).zeroTermsQuery(ZeroTermsQuery.ALL);

    SearchQuery searchQuery =
        new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable).build();

    return this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableRepositoryCustom#
   * matchQueryInAllFieldAndNgrams(java.lang.String, java.lang.String,
   * org.springframework.data.domain.Pageable)
   */
  @Override
  public Page<VariableDocument> matchQueryInAllFieldAndNgrams(String query, String scaleLevel,
      Pageable pageable) {
    
    //create search query
    QueryBuilder queryBuilder =
        boolQuery().should(matchQuery("_all", query).zeroTermsQuery(ZeroTermsQuery.NONE))
            .should(matchQuery(VariableDocument.ALL_STRINGS_AS_NGRAMS_FIELD, query)
                .minimumShouldMatch(minimumShouldMatch));

    //create filter for scale level
    FilterBuilder filterBuilder = null;
    if (scaleLevel != null) {
      filterBuilder = FilterBuilders.boolFilter()
          .must(FilterBuilders.termFilter(VariableDocument.SCALE_LEVEL_FIELD, scaleLevel));
    }
    
    //extended search query with filter
    SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder)
        .withFilter(filterBuilder).withPageable(pageable).build();

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

    QueryBuilder queryBuilder =
        QueryBuilders.matchPhrasePrefixQuery("_all", query).fuzziness(Fuzziness.AUTO);

    SearchQuery searchQuery =
        new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable).build();

    return this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableRepositoryCustom#
   * matchFilterBySurveyId(java.lang.String, java.lang.String)
   */
  @Override
  public Page<VariableDocument> filterBySurveyIdAndVariableAlias(String surveyId,
      String variableAlias) {

    QueryBuilder queryBuilder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
        FilterBuilders.nestedFilter(VariableDocument.VARIABLE_SURVEY_FIELD,
            FilterBuilders.boolFilter().must(
                FilterBuilders.termFilter(VariableDocument.NESTED_VARIABLE_SURVEY_ID_FIELD,
                    surveyId),
                FilterBuilders.termFilter(
                    VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD, variableAlias))));

    SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();

    return this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class);
  }
}
