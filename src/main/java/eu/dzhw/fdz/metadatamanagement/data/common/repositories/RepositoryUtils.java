package eu.dzhw.fdz.metadatamanagement.data.common.repositories;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.index.query.MatchQueryBuilder.ZeroTermsQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder.Field;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.DocumentField;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.AggregationType;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.FilterType;

/**
 * This Util class support the generation of queries with filter and aggrogration of any kind of
 * document.
 * 
 * @author Daniel Katzberg
 *
 */
public class RepositoryUtils {

  /**
   * Yeah. TODO
   * @param searchFilter Yeah.
   * @param pageable Yeah.
   * @param minimumShouldMatch Yeah.
   * @return Yeah.
   */
  @SuppressWarnings("rawtypes")
  public static SearchQuery createSearchQuery(AbstractSearchFilter searchFilter, Pageable pageable,
      String minimumShouldMatch) {

    // create search query (with filter)
    QueryBuilder queryBuilder =
        RepositoryUtils.createQueryBuilder(searchFilter.getQuery(), minimumShouldMatch);
    List<FilterBuilder> termFilterBuilders = RepositoryUtils
        .createFilterBuilders(searchFilter.getAllFilterValues(), searchFilter.getAllFilterTypes());
    queryBuilder = RepositoryUtils.addFilterToQuery(queryBuilder, termFilterBuilders);

    // prepare search query (with filter)
    NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
    nativeSearchQueryBuilder.withQuery(queryBuilder);

    // add Aggregations
    List<AggregationBuilder> aggregationBuilders =
        RepositoryUtils.createAggregations(searchFilter.getAllAggregationTypes());
    for (AggregationBuilder aggregationBuilder : aggregationBuilders) {
      nativeSearchQueryBuilder.addAggregation(aggregationBuilder);
    }

    // Add Highlighting fields
    HighlightBuilder.Field[] fields =
        RepositoryUtils.createHighlightFields(searchFilter.getSupportedHighlightingFields());
    nativeSearchQueryBuilder.withHighlightFields(fields);

    // Create search query
    return nativeSearchQueryBuilder.withPageable(pageable).build();
  }

  /**
   * Creates all aggregation for the buckets of the filter information.
   * 
   * @param aggregationFields all known filters.
   * @return a list of aggregations builder for the aggregation information.
   */
  @SuppressWarnings("rawtypes")
  public static List<AggregationBuilder> createAggregations(
      Map<DocumentField, AggregationType> aggregationFields) {
    List<AggregationBuilder> aggregationBuilders = new ArrayList<>();

    // add nested or not nested aggregations
    for (Entry<DocumentField, AggregationType> aggregationField : aggregationFields.entrySet()) {
      AggregationBuilder aggregationBuilder = RepositoryUtils
          .createNestedAggregationBuilder(aggregationField.getKey(), aggregationField.getValue());
      // check for default case (no aggregation filter was found
      if (aggregationBuilder != null) {
        aggregationBuilders.add(aggregationBuilder);
      }
    }

    return aggregationBuilders;
  }

  /**
   * This is a helper method for the creation of nested aggregations. It can create non nested
   * aggregations to. It supports any nested depth by recursion.
   * 
   * @param field a given field with the path name and a optional nested field
   * @return an nested or not nested aggregations
   */
  @SuppressWarnings("rawtypes")
  public static AggregationBuilder createNestedAggregationBuilder(DocumentField field,
      AggregationType aggregationType) {

    AggregationBuilder aggregationBuilder;

    switch (aggregationType) {
      case TERM:
        aggregationBuilder =
            AggregationBuilders.terms(field.getAbsolutePath()).field(field.getAbsolutePath());
        break;
      default:
        throw new IllegalStateException("Undefined filter type!");
    }

    // wrap nested aggregations if necessary
    while (field.isNested()) {
      field = field.getParent();
      aggregationBuilder = AggregationBuilders.nested(field.getAbsolutePath())
          .path(field.getAbsolutePath()).subAggregation(aggregationBuilder);
    }

    return aggregationBuilder;
  }

  /**
   * returns a basic query builder without filter.
   * 
   * @param minimumShouldMatch the minimum should match value for the ngram fields.
   * @param query The request parameter value of the query
   * @return Returns a query builder
   */
  public static QueryBuilder createQueryBuilder(String query, String minimumShouldMatch) {
    QueryBuilder queryBuilder = null;
    if (StringUtils.hasText(query)) {
      queryBuilder = boolQuery()
          .should(
              matchQuery("_all", query).zeroTermsQuery(ZeroTermsQuery.NONE).operator(Operator.AND))
          .should(matchQuery(AbstractDocument.ALL_STRINGS_AS_NGRAMS_FIELD.getAbsolutePath(), query)
              .zeroTermsQuery(ZeroTermsQuery.NONE).operator(Operator.AND)
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
   * @param filterValues A map. The key is the filter field and the object is the value of the
   *        filter
   * @param filterTypes A map. The key is the filter field and the value is the type of the filter
   * @return a list of all filter
   * 
   * @see FilterBuilders
   * @see FilterBuilder
   */
  public static List<FilterBuilder> createFilterBuilders(Map<DocumentField, String> filterValues,
      Map<DocumentField, FilterType> filterTypes) {
    List<FilterBuilder> filterBuilders = new ArrayList<>();

    // create nested or not nested filter.
    for (Entry<DocumentField, String> entry : filterValues.entrySet()) {

      FilterType filterType = filterTypes.get(entry.getKey());

      FilterBuilder filterBuilder =
          RepositoryUtils.createNestedFilterBuilder(entry.getKey(), entry.getValue(), filterType);
      // catch the default case with return null of the create method.
      if (filterBuilder != null) {
        filterBuilders.add(filterBuilder);
      }
    }

    return filterBuilders;
  }

  /**
   * This is a helper method for the creation of nested filter. It supports not nested filter, too.
   * And it supports any nested depth by recursion.
   * 
   * @param field A field with a given path of the nested path and nested fields.
   * @param value the value of the request parameter / value of the field
   * @param filterType The value is the type of the filter
   * @return A nested or not nested FilterBuilder
   */
  public static FilterBuilder createNestedFilterBuilder(DocumentField field, String value,
      FilterType filterType) {

    FilterBuilder filterBuilder;
    switch (filterType) {
      case TERM:
        filterBuilder = FilterBuilders.termFilter(field.getAbsolutePath(), value);
        break;
      case RANGE_GTE:
        filterBuilder = FilterBuilders.rangeFilter(field.getAbsolutePath()).gte(value);
        break;
      case RANGE_LTE:
        filterBuilder = FilterBuilders.rangeFilter(field.getAbsolutePath()).lte(value);
        break;
      default:
        throw new IllegalStateException("Undefined filter type!");
    }

    // wrap nested filters around the specific filter if necessary
    while (field.isNested()) {
      field = field.getParent();
      filterBuilder = FilterBuilders.nestedFilter(field.getAbsolutePath(), filterBuilder);
    }
    return filterBuilder;
  }


  /**
   * @param supportedHighlightingFields A list with all field which are used for highlighting by
   *        elasticsearch.
   * @return A array with {@link HighlightBuilder.Field} elements for the highlighting.
   */
  public static Field[] createHighlightFields(List<DocumentField> supportedHighlightingFields) {
    HighlightBuilder.Field[] fields =
        new HighlightBuilder.Field[supportedHighlightingFields.size()];

    for (int i = 0; i < supportedHighlightingFields.size(); i++) {
      fields[i] = new HighlightBuilder.Field(
          supportedHighlightingFields.get(i).getAbsolutePath() + ".highlight");
    }

    return fields;
  }

  /**
   * Adds all termfilter to a query.
   * 
   * @param queryBuilder A query builder for creation of a query
   * @param termFilterBuilders all term filters for the query.
   * @return a query builder with filter
   */
  public static QueryBuilder addFilterToQuery(QueryBuilder queryBuilder,
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

}
