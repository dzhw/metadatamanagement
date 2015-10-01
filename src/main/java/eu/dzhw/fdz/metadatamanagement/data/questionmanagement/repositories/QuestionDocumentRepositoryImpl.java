package eu.dzhw.fdz.metadatamanagement.data.questionmanagement.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import eu.dzhw.fdz.metadatamanagement.data.common.repositories.RepositoryUtils;
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

  @Autowired
  public QuestionDocumentRepositoryImpl(ElasticsearchTemplate elasticsearchTemplate) {
    this.elasticsearchTemplate = elasticsearchTemplate;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.questionmanagement.repositories.
   * QuestionDocumentRepositoryCustom#search(eu.dzhw.fdz.metadatamanagement.web.common.dtos.
   * AbstractSearchFilter, org.springframework.data.domain.Pageable)
   */
  @Override
  public FacetedPage<QuestionDocument> search(AbstractSearchFilter searchFilter,
      Pageable pageable) {

    // Create search query
    SearchQuery searchQuery =
        RepositoryUtils.createSearchQuery(searchFilter, pageable, this.minimumShouldMatch);

    // No Problems with thread safe queries, because every query has an own mapper
    FacetedPage<QuestionDocument> facetedPage =
        this.elasticsearchTemplate.queryForPage(searchQuery, QuestionDocument.class);

    // return pageable object
    return facetedPage;
  }
}
