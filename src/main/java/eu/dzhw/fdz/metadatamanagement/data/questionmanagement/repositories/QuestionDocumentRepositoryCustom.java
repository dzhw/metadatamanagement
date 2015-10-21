package eu.dzhw.fdz.metadatamanagement.data.questionmanagement.repositories;

import org.springframework.data.domain.Pageable;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.PageWithBuckets;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionDocument;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.SearchFilter;

/**
 * This is the interface for custom methods of the repository for the question documents.
 * 
 * @author Daniel Katzberg
 *
 */
public interface QuestionDocumentRepositoryCustom {

  /**
   * Query for all question documents by using the all field for exact matches as well as fuzzy
   * matches.
   * 
   * @param abstractSearchFilter the data transfer object of the search
   * @param pageable The page size and number and sort.
   * @return A page holding the first question documents
   */
  PageWithBuckets<QuestionDocument> search(SearchFilter searchFilter, Pageable pageable);

}
