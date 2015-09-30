package eu.dzhw.fdz.metadatamanagement.service.questionmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.PageWithBuckets;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionDocument;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.repositories.QuestionDocumentRepository;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter;

/**
 * A service for searching questions.
 * 
 * @author Daniel Katzberg
 *
 */
@Service
public class QuestionService {

  private final QuestionDocumentRepository questionRepository;

  /**
   * @param questionRepository A reference to the repository for question document.
   */
  @Autowired
  public QuestionService(QuestionDocumentRepository questionRepository) {
    this.questionRepository = questionRepository;
  }

  /**
   * Search questions by query. If the query string does not contain text the first n questions are
   * returned as defined in the pageable.
   * 
   * @param searchFilter The searchfilter with all information from the query (filter, query etc)
   * @param pageable a pageable object.
   * 
   * @return Page with the found QuestionDocument
   */
  public PageWithBuckets<QuestionDocument> search(AbstractSearchFilter searchFilter,
      Pageable pageable) {
    return this.questionRepository.search(searchFilter, pageable);

  }

  /**
   * Load question by id.
   * 
   * @param id the id for the document.
   * 
   * @return QuestionDocument
   */
  public QuestionDocument get(String id) {
    return this.questionRepository.findOne(id);
  }

  /**
   * Saves a question document to the repository.
   * 
   * @param questionDocument The QuestionDocument which should be save.
   * @return The saved QuestionDocument
   * @see QuestionDocument
   */
  public QuestionDocument save(QuestionDocument questionDocument) {
    return this.questionRepository.save(questionDocument);
  }

  /**
   * Deletes a question document from the repository by a given id.
   * 
   * @param id The id of the variable document.
   */
  public void delete(String id) {
    this.questionRepository.delete(id);
  }

}
