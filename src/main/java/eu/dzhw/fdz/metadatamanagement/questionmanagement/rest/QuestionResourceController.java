package eu.dzhw.fdz.metadatamanagement.questionmanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;

/**
 * Question REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
public class QuestionResourceController 
    extends GenericDomainObjectResourceController<Question, QuestionRepository> {

  @Autowired
  public QuestionResourceController(QuestionRepository questionRepository) {
    super(questionRepository);
  }

  /**
   * Override default get by id since it does not set cache headers correctly.
   * 
   * @param id a Question id
   * @return the Question or not found
   */
  @RequestMapping(method = RequestMethod.GET, value = "/questions/{id:.+}")
  public ResponseEntity<Question> findQuestion(@PathVariable String id) {
    return super.findDomainObject(id);
  }
}
