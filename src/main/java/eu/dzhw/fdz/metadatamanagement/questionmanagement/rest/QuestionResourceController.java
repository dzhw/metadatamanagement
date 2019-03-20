package eu.dzhw.fdz.metadatamanagement.questionmanagement.rest;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericShadowableDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

/**
 * Question REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
public class QuestionResourceController
    extends GenericShadowableDomainObjectResourceController<Question, QuestionRepository> {
  
  @Autowired
  public QuestionResourceController(QuestionRepository questionRepository,
                                    ApplicationEventPublisher applicationEventPublisher) {
    super(questionRepository, applicationEventPublisher);
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

  /**
   * Override default post to prevent creating shadow copies.
   * @param question Question
   */
  @RequestMapping(method = RequestMethod.POST, value = "/questions")
  public ResponseEntity<?> postQuestion(@Valid @RequestBody Question question) {
    return super.postDomainObject(question);
  }

  /**
   * Override default put to prevent creating or updating shadow copies.
   * @param id Question id
   * @param question Question data
   */
  @RequestMapping(method = RequestMethod.PUT, value = "/questions/{id:.+}")
  public ResponseEntity<?> putQuestion(@PathVariable String id,
                                       @Valid @RequestBody Question question) {
    return super.putDomainObject(id, question);
  }

  /**
   * Override default delete to prevent deleting shadow copies.
   * @param id Question id
   */
  @RequestMapping(method = RequestMethod.DELETE, value = "/questions/{id:.+}")
  public ResponseEntity<?> deleteQuestion(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(Question domainObject) {
    return UriComponentsBuilder.fromPath("/api/questions/" + domainObject.getId()).build()
        .toUri();
  }
}
