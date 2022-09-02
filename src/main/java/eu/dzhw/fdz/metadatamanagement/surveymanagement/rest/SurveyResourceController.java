package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

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
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Survey REST Controller which overrides default spring data rest methods.
 *
 * @author René Reitmann
 */
@Controller
@Tag(name = "Survey Resource", description = "Endpoints used by the MDM to manage surveys.")
public class SurveyResourceController
    extends GenericDomainObjectResourceController<Survey, CrudService<Survey>> {

  public SurveyResourceController(CrudService<Survey> crudService) {
    super(crudService);
  }

  @Override
  @Operation(summary = "Get the survey. Public users will get the latest version of the survey."
      + " If the id is postfixed with the version number it will return exactly the "
      + "requested version, if available.")
  @GetMapping(value = "/api/surveys/{id:.+}")
  @ResponseBody
  public ResponseEntity<Survey> getDomainObject(@PathVariable String id) {
    return super.getDomainObject(id);
  }


  @Override
  @PostMapping(value = "/api/surveys")
  public ResponseEntity<?> postDomainObject(@RequestBody Survey survey) {
    return super.postDomainObject(survey);
  }

  @Override
  @PutMapping(value = "/api/surveys/{id:.+}")
  public ResponseEntity<?> putDomainObject(@RequestBody Survey survey) {
    return super.putDomainObject(survey);
  }

  @Override
  @DeleteMapping("/api/surveys/{id:.+}")
  public ResponseEntity<?> deleteDomainObject(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(Survey domainObject) {
    return UriComponentsBuilder.fromPath("/api/surveys/" + domainObject.getId()).build().toUri();
  }
}
