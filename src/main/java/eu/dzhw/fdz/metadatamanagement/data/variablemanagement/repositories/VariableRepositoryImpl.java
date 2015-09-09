package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories;


import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
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

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.AggregationResultMapper;
import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.PageWithBuckets;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.VariableSearchFormDto;

/**
 * This class implements the interface of the custom variable documents repository. This class will
 * be used for the implementation of the repository beans.
 * 
 * @author Daniel Katzberg
 *
 */
public class VariableRepositoryImpl implements VariableRepositoryCustom {

  /**
   * This String defines a limit by searching within ngrams. The limit is: how many ngrams of a
   * searching word should be match another ngrams from a word in the database for a valid result.
   */
  @Value("${search.minimum-should-match-ngrams}")
  private String minimumShouldMatch;

  /**
   * A elasticsearch template for start queries.
   */
  private ElasticsearchTemplate elasticsearchTemplate;

  /**
   * This result mapper support a facedpage with buckets. The default mapper does not support the
   * opportunity the returning of the buckets of the aggregations.
   */
  private AggregationResultMapper resultMapper;

  @Autowired
  public VariableRepositoryImpl(ElasticsearchTemplate elasticsearchTemplate,
      AggregationResultMapper resultMapper) {
    this.elasticsearchTemplate = elasticsearchTemplate;
    this.resultMapper = resultMapper;
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
   * matchQueryInAllFieldAndNgrams(eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.
   * SearchFormDto, org.springframework.data.domain.Pageable)
   */
  @Override
  public PageWithBuckets<VariableDocument> matchQueryInAllFieldAndNgrams(
      VariableSearchFormDto formDto, Pageable pageable) {

    NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

    QueryBuilder queryBuilder = null;
    // create search query
    if (StringUtils.hasText(formDto.getQuery())) {
      queryBuilder = boolQuery()
          .should(matchQuery("_all", formDto.getQuery()).zeroTermsQuery(ZeroTermsQuery.NONE))
          .should(matchQuery(VariableDocument.ALL_STRINGS_AS_NGRAMS_FIELD, formDto.getQuery())
              .minimumShouldMatch(minimumShouldMatch));
    } else {
      // Match all case if there is an aggregation with a filter but without a query
      queryBuilder = matchAllQuery();
    }

    // create filter and aggregation for scale level
    if (StringUtils.hasText(formDto.getScaleLevel())) {
      FilterBuilder filterBuilder =
          FilterBuilders.termFilter(VariableDocument.SCALE_LEVEL_FIELD, formDto.getScaleLevel());

      // do not use nativeSearchQueryBuilder.withFilter(). It uses the post_filter!
      queryBuilder = QueryBuilders.filteredQuery(queryBuilder, filterBuilder);
    }

    // add query (with filter)
    nativeSearchQueryBuilder.withQuery(queryBuilder);

    // extended search query with filter
    SearchQuery searchQuery = nativeSearchQueryBuilder
        .withPageable(pageable).addAggregation(AggregationBuilders
            .terms(VariableDocument.SCALE_LEVEL_FIELD).field(VariableDocument.SCALE_LEVEL_FIELD))
        .build();

    // No Problems with thread safe queries, because every query has an own mapper
    FacetedPage<VariableDocument> facetedPage =
        this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class, resultMapper);

    // return pageable object and the aggregations
    return (PageWithBuckets<VariableDocument>) facetedPage;
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

    QueryBuilder queryBuilder = QueryBuilders.filteredQuery(matchAllQuery(),
        FilterBuilders.nestedFilter(VariableDocument.VARIABLE_SURVEY_FIELD,
            FilterBuilders.boolFilter().must(
                FilterBuilders.termFilter(VariableDocument.NESTED_VARIABLE_SURVEY_ID_FIELD,
                    surveyId),
            FilterBuilders.termFilter(VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD,
                variableAlias))));

    SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();

    return this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableRepositoryCustom#
   * matchAllWithAggregations(org.springframework.data.domain.Pageable)
   */
  @Override
  public PageWithBuckets<VariableDocument> matchAllWithAggregations(Pageable pageable) {
    NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

    QueryBuilder queryBuilder = matchAllQuery();
    nativeSearchQueryBuilder.withQuery(queryBuilder);

    // create aggregation for scaleLevel
    nativeSearchQueryBuilder.addAggregation(AggregationBuilders
        .terms(VariableDocument.SCALE_LEVEL_FIELD).field(VariableDocument.SCALE_LEVEL_FIELD));

    SearchQuery searchQuery = nativeSearchQueryBuilder.withPageable(pageable).build();

    // No Problems with thread safe queries, because every query has an own mapper
    FacetedPage<VariableDocument> facetedPage =
        this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class, resultMapper);

    // return pageable object and the aggregations
    return (PageWithBuckets<VariableDocument>) facetedPage;
  }
}
