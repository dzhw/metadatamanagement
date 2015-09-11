package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories;


import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MatchQueryBuilder.ZeroTermsQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
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
   * search(eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.VariableSearchFormDto,
   * org.springframework.data.domain.Pageable)
   */
  @SuppressWarnings("rawtypes")
  @Override
  public PageWithBuckets<VariableDocument> search(VariableSearchFormDto formDto,
      Pageable pageable) {

    // create search query (with filter)
    QueryBuilder queryBuilder = this.createQueryBuilder(formDto.getQuery());
    List<FilterBuilder> termFilterBuilders = this.createTermFilterBuilders(formDto.getAllFilters());
    queryBuilder = this.addFilterToQuery(queryBuilder, termFilterBuilders);

    // prepare search query (with filter)
    NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
    nativeSearchQueryBuilder.withQuery(queryBuilder);

    // add Aggregations
    List<AggregationBuilder> aggregationBuilders =
        this.createAggregations(formDto.getFilterNames());
    for (AggregationBuilder aggregationBuilder : aggregationBuilders) {
      nativeSearchQueryBuilder.addAggregation(aggregationBuilder);
    }

    // Create search query
    SearchQuery searchQuery = nativeSearchQueryBuilder.withPageable(pageable).build();

    // No Problems with thread safe queries, because every query has an own mapper
    FacetedPage<VariableDocument> facetedPage =
        this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class, resultMapper);

    // return pageable object and the aggregations
    return (PageWithBuckets<VariableDocument>) facetedPage;
  }

  /**
   * Adds all termfilter to a query.
   * 
   * @param queryBuilder A query builder for creation of a query
   * @param termFilterBuilders all term filters for the query.
   * @return a query builder with filter
   */
  private QueryBuilder addFilterToQuery(QueryBuilder queryBuilder,
      List<FilterBuilder> termFilterBuilders) {
    if (!termFilterBuilders.isEmpty()) {

      // add 1..* term filter
      BoolFilterBuilder filterBoolBuilder = FilterBuilders.boolFilter();
      for (FilterBuilder termFilterBuilder : termFilterBuilders) {
        filterBoolBuilder = filterBoolBuilder.must(termFilterBuilder);
      }

      // add bool filter to query
      queryBuilder = QueryBuilders.filteredQuery(queryBuilder, filterBoolBuilder);
    }

    return queryBuilder;
  }

  /**
   * Creates all aggregation for the buckets of the filter information.
   * 
   * @param filterNames all known filters.
   * @return a list of aggregations builder for the aggregation information.
   */
  @SuppressWarnings("rawtypes")
  private List<AggregationBuilder> createAggregations(List<String> filterNames) {
    List<AggregationBuilder> aggregationBuilders = new ArrayList<>();

    for (String filterName : filterNames) {
      if (VariableSearchFormDto.isNestedFilter(filterName)) {
        aggregationBuilders
            .add(AggregationBuilders.nested(VariableSearchFormDto.getBasicPath(filterName))
                .path(VariableSearchFormDto.getBasicPath(filterName))
                .subAggregation(AggregationBuilders.terms(filterName).field(filterName)));
      } else {
        aggregationBuilders.add(AggregationBuilders.terms(filterName).field(filterName));
      }
    }

    return aggregationBuilders;
  }

  /**
   * returns a basic query builder without filter.
   * 
   * @param query The request parameter value of the query
   * @return Returns a query builder
   */
  private QueryBuilder createQueryBuilder(String query) {
    QueryBuilder queryBuilder = null;
    if (StringUtils.hasText(query)) {
      queryBuilder =
          boolQuery().should(matchQuery("_all", query).zeroTermsQuery(ZeroTermsQuery.NONE))
              .should(matchQuery(VariableDocument.ALL_STRINGS_AS_NGRAMS_FIELD, query)
                  .minimumShouldMatch(minimumShouldMatch));
    } else {
      // Match all case if there is an aggregation with a filter but without a query
      queryBuilder = matchAllQuery();
    }

    return queryBuilder;
  }

  /**
   * This method creates the term filter builder. It differs between nested and flat filter.
   * 
   * @param filterValues A map. The key is the filter name and the object is the value of the filter
   * @return a list of all filter
   * 
   * @see FilterBuilders
   * @see FilterBuilder
   */
  private List<FilterBuilder> createTermFilterBuilders(Map<String, String> filterValues) {
    List<FilterBuilder> termFilterBuilders = new ArrayList<>();
    for (Entry<String, String> entry : filterValues.entrySet()) {

      if (VariableSearchFormDto.isNestedFilter(entry.getKey())) {
        termFilterBuilders
            .add(FilterBuilders.nestedFilter(VariableSearchFormDto.getBasicPath(entry.getKey()),
                FilterBuilders.termFilter(entry.getKey(), entry.getValue())));
      } else {
        termFilterBuilders.add(FilterBuilders.termFilter(entry.getKey(), entry.getValue()));
      }
    }

    return termFilterBuilders;
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
