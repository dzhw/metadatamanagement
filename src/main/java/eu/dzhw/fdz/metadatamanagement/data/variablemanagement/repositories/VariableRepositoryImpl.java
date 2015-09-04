package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories;


import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MatchQueryBuilder.ZeroTermsQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.datatype.PageableAggregrationType;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.mapper.AggregationResultMapper;

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
  public PageableAggregrationType<VariableDocument> matchQueryInAllFieldAndNgrams(String query,
      String scaleLevel, Pageable pageable) {

    NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

    // create search query
    if (StringUtils.hasText(query)) {
      QueryBuilder queryBuilder =
          boolQuery().should(matchQuery("_all", query).zeroTermsQuery(ZeroTermsQuery.NONE))
              .should(matchQuery(VariableDocument.ALL_STRINGS_AS_NGRAMS_FIELD, query)
                  .minimumShouldMatch(minimumShouldMatch));
      nativeSearchQueryBuilder.withQuery(queryBuilder);
    }


    // create filter and aggregation for scale level
    if (StringUtils.hasText(scaleLevel)) {
      FilterBuilder filterBuilder = FilterBuilders.boolFilter()
          .must(FilterBuilders.termFilter(VariableDocument.SCALE_LEVEL_FIELD, scaleLevel));
      nativeSearchQueryBuilder.withFilter(filterBuilder);
    }

    // extended search query with filter
    SearchQuery searchQuery = nativeSearchQueryBuilder
        .withPageable(pageable).addAggregation(AggregationBuilders
            .terms(VariableDocument.SCALE_LEVEL_FIELD).field(VariableDocument.SCALE_LEVEL_FIELD))
        .build();

    // No Problems with thread safe queries, because every query has an own mapper
    AggregationResultMapper resultMapper = new AggregationResultMapper();
    FacetedPage<VariableDocument> facetPages =
        this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class, resultMapper);

    // return pageable object and the aggregations
    return new PageableAggregrationType<VariableDocument>(facetPages,
        resultMapper.getAggregations());
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
