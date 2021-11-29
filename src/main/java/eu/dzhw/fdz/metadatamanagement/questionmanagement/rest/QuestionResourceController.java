package eu.dzhw.fdz.metadatamanagement.questionmanagement.rest;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Question REST Controller which overrides default spring data rest methods.
 *
 * @author René Reitmann
 */
@Controller
@Tag(name = "Question Resource", description = "Endpoints used by the MDM to manage questions.")
public class QuestionResourceController
    extends GenericDomainObjectResourceController<Question, CrudService<Question>> {

  public QuestionResourceController(CrudService<Question> crudService) {
    super(crudService);
  }

  @Override
  @Operation(summary = "Get the question. Public users will get the latest version of the question."
      + " If the id is postfixed with the version number it will return exactly the "
      + "requested version, if available.")
  @GetMapping(value = "/api/questions/{id:.+}")
  @ResponseBody
  public ResponseEntity<Question> getDomainObject(@PathVariable String id) {
    return super.getDomainObject(id);
  }


  @Override
  @PostMapping(value = "/api/questions")
  public ResponseEntity<?> postDomainObject(@RequestBody Question question) {
    return super.postDomainObject(question);
  }

  @Override
  @PutMapping(value = "/api/questions/{id:.+}")
  public ResponseEntity<?> putDomainObject(@RequestBody Question question) {
    return super.putDomainObject(question);
  }

  @Override
  @DeleteMapping("/api/questions/{id:.+}")
  public ResponseEntity<?> deleteDomainObject(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(Question domainObject) {
    return UriComponentsBuilder.fromPath("/api/questions/" + domainObject.getId()).build().toUri();
  }
}
