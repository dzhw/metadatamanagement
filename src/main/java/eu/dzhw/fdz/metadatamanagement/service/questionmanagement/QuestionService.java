package eu.dzhw.fdz.metadatamanagement.service.questionmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.PageWithBuckets;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionDocument;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.repositories.QuestionDocumentRepository;
import eu.dzhw.fdz.metadatamanagement.service.common.SearchService;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter;

/**
 * A service for searching questions.
 * 
 * @author Daniel Katzberg
 *
 */
@Service
public class QuestionService implements SearchService<QuestionDocument> {

  private final QuestionDocumentRepository questionRepository;

  /**
   * @param questionRepository A reference to the repository for question document.
   */
  @Autowired
  public QuestionService(QuestionDocumentRepository questionRepository) {
    this.questionRepository = questionRepository;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.service.common.SearchService#search(eu.dzhw.fdz.
   * metadatamanagement.web.common.dtos.AbstractSearchFilter,
   * org.springframework.data.domain.Pageable)
   */
  @Override
  public PageWithBuckets<QuestionDocument> search(AbstractSearchFilter searchFilter,
      Pageable pageable) {
    return this.questionRepository.search(searchFilter, pageable);
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.service.common.BasicService#get(java.lang.String)
   */
  @Override
  public QuestionDocument get(String id) {
    return this.questionRepository.findOne(id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.service.common.BasicService#save(java.lang.Object)
   */
  @Override
  public QuestionDocument save(QuestionDocument questionDocument) {
    return this.questionRepository.save(questionDocument);
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.service.common.BasicService#delete(java.lang.String)
   */
  @Override
  public void delete(String id) {
    this.questionRepository.delete(id);
  }

}
