package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

import java.net.URI;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Study REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
@Tag(name = "Study Resource", description = "Endpoints used by the MDM to manage studies.")
@RequestMapping("/api")
public class StudyResourceController
    extends GenericDomainObjectResourceController<Study, CrudService<Study>> {

  public StudyResourceController(CrudService<Study> crudService,
      UserInformationProvider userInformationProvider) {
    super(crudService, userInformationProvider);
  }

  @Override
  @Operation(summary = "Get the study. Public users will get the latest version of the study."
      + " If the id is postfixed with the version number it will return exactly the "
      + "requested version, if available.")
  @GetMapping(value = "/studies/{id:.+}")
  public ResponseEntity<Study> getDomainObject(@PathVariable String id) {
    return super.getDomainObject(id);
  }

  @Override
  @PostMapping(value = "/studies")
  public ResponseEntity<?> postDomainObject(@RequestBody Study study) {
    return super.postDomainObject(study);
  }

  @Override
  @PutMapping(value = "/studies/{id:.+}")
  public ResponseEntity<?> putDomainObject(@RequestBody Study study) {
    return super.putDomainObject(study);
  }

  @Override
  @DeleteMapping("/studies/{id:.+}")
  public ResponseEntity<?> deleteDomainObject(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(Study domainObject) {
    return UriComponentsBuilder.fromPath("/api/studies/" + domainObject.getId()).build().toUri();
  }
}
