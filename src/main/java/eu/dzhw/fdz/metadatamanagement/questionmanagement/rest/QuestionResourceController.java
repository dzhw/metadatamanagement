package eu.dzhw.fdz.metadatamanagement.questionmanagement.rest;

import java.net.URI;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;

/**
 * Question REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
public class QuestionResourceController extends GenericDomainObjectResourceController
    <Question, CrudService<Question>> {

  public QuestionResourceController(CrudService<Question> crudService) {
    super(crudService);
  }

  @Override
  @GetMapping(value = "/questions/{id:.+}")
  public ResponseEntity<Question> getDomainObject(@PathVariable String id) {
    return super.getDomainObject(id);
  }

 
  @Override
  @PostMapping(value = "/questions")
  public ResponseEntity<?> postDomainObject(@RequestBody Question question) {
    return super.postDomainObject(question);
  }

  @Override
  @PutMapping(value = "/questions/{id:.+}")
  public ResponseEntity<?> putDomainObject(@RequestBody Question question) {
    return super.putDomainObject(question);
  }

  @Override
  @DeleteMapping("/questions/{id:.+}")
  public ResponseEntity<?> deleteDomainObject(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(Question domainObject) {
    return UriComponentsBuilder.fromPath("/api/questions/" + domainObject.getId()).build().toUri();
  }
}
