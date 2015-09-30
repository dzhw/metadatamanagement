package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories;


import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.PageWithBuckets;
import eu.dzhw.fdz.metadatamanagement.data.common.repositories.RepositoryUtils;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.aggregations.VariableDocumentAggregrationResultMapper;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter;

/**
 * This class implements the interface of the custom variable documents repository. This class will
 * be used for the implementation of the repository beans.
 * 
 * @author Daniel Katzberg
 *
 */
public class VariableDocumentRepositoryImpl implements VariableDocumentRepositoryCustom {

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
  private VariableDocumentAggregrationResultMapper resultMapper;

  @Autowired
  public VariableDocumentRepositoryImpl(ElasticsearchTemplate elasticsearchTemplate,
      VariableDocumentAggregrationResultMapper resultMapper) {
    this.elasticsearchTemplate = elasticsearchTemplate;
    this.resultMapper = resultMapper;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.
   * VariableDocumentRepositoryCustom#search(eu.dzhw.fdz.metadatamanagement.web.common.dtos.
   * AbstractSearchFilter, org.springframework.data.domain.Pageable)
   */
  @Override
  public PageWithBuckets<VariableDocument> search(AbstractSearchFilter searchFilter,
      Pageable pageable) {

    // Create search query
    SearchQuery searchQuery =
        RepositoryUtils.createSearchQuery(searchFilter, pageable, this.minimumShouldMatch);

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
   * eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableRepositoryCustom#
   * matchFilterBySurveyId(java.lang.String, java.lang.String)
   */
  @Override
  public Page<VariableDocument> filterBySurveyIdAndVariableAlias(String surveyId,
      String variableAlias) {

    QueryBuilder queryBuilder = QueryBuilders.filteredQuery(matchAllQuery(),
        FilterBuilders.nestedFilter(VariableDocument.VARIABLE_SURVEY_FIELD.getAbsolutePath(),
            FilterBuilders.boolFilter()
                .must(FilterBuilders.termFilter(
                    VariableDocument.NESTED_VARIABLE_SURVEY_ID_FIELD.getAbsolutePath(), surveyId),
                FilterBuilders.termFilter(
                    VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD.getAbsolutePath(),
                    variableAlias))));

    SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();

    return this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class);
  }
}
