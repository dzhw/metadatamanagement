package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
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
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;

/**
 * Survey REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
public class SurveyResourceController extends GenericDomainObjectResourceController
    <Survey, CrudService<Survey>> {

  @Autowired
  public SurveyResourceController(CrudService<Survey> crudService) {
    super(crudService);
  }

  @Override
  @GetMapping(value = "/surveys/{id:.+}")
  public ResponseEntity<Survey> getDomainObject(@PathVariable String id) {
    return super.getDomainObject(id);
  }

 
  @Override
  @PostMapping(value = "/surveys")
  public ResponseEntity<?> postDomainObject(@RequestBody Survey survey) {
    return super.postDomainObject(survey);
  }

  @Override
  @PutMapping(value = "/surveys/{id:.+}")
  public ResponseEntity<?> putDomainObject(@RequestBody Survey survey) {
    return super.putDomainObject(survey);
  }

  @Override
  @DeleteMapping("/surveys/{id:.+}")
  public ResponseEntity<?> deleteDomainObject(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(Survey domainObject) {
    return UriComponentsBuilder.fromPath("/api/surveys/" + domainObject.getId()).build().toUri();
  }
}
