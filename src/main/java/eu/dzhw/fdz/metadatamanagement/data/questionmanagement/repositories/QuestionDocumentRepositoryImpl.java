package eu.dzhw.fdz.metadatamanagement.data.questionmanagement.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.PageWithBuckets;
import eu.dzhw.fdz.metadatamanagement.data.common.repositories.RepositoryUtils;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.aggregations.QuestionDocumentAggregationResultMapper;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionDocument;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter;

/**
 * This class implements the interface of the custom variable documents repository. This class will
 * be used for the implementation of the repository beans.
 * 
 * @author Daniel Katzberg
 *
 */
public class QuestionDocumentRepositoryImpl implements QuestionDocumentRepositoryCustom {

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
  private QuestionDocumentAggregationResultMapper resultMapper;

  @Autowired
  public QuestionDocumentRepositoryImpl(ElasticsearchTemplate elasticsearchTemplate,
      QuestionDocumentAggregationResultMapper resultMapper) {
    this.elasticsearchTemplate = elasticsearchTemplate;
    this.resultMapper = resultMapper;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.questionmanagement.repositories.
   * QuestionDocumentRepositoryCustom#search(eu.dzhw.fdz.metadatamanagement.web.common.dtos.
   * AbstractSearchFilter, org.springframework.data.domain.Pageable)
   */
  @Override
  public PageWithBuckets<QuestionDocument> search(AbstractSearchFilter searchFilter,
      Pageable pageable) {

    // Create search query
    SearchQuery searchQuery =
        RepositoryUtils.createSearchQuery(searchFilter, pageable, this.minimumShouldMatch);

    // No Problems with thread safe queries, because every query has an own mapper
    FacetedPage<QuestionDocument> facetedPage = this.elasticsearchTemplate.queryForPage(searchQuery,
        QuestionDocument.class, this.resultMapper);

    // return pageable object
    return (PageWithBuckets<QuestionDocument>) facetedPage;
  }
}
