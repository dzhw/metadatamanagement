package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.rest;

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
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * RelatedPublication REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
@Api(value = "Publication Resource",
    description = "Endpoints used by the MDM to manage publications.")
public class RelatedPublicationResourceController extends
    GenericDomainObjectResourceController<RelatedPublication, CrudService<RelatedPublication>> {

  public RelatedPublicationResourceController(CrudService<RelatedPublication> crudService,
      UserInformationProvider userInformationProvider) {
    super(crudService, userInformationProvider);
  }

  @Override
  @ApiOperation("Get the publication.")
  @GetMapping(value = "/related-publications/{id:.+}")
  public ResponseEntity<RelatedPublication> getDomainObject(@PathVariable String id) {
    return super.getDomainObject(id);
  }


  @Override
  @PostMapping(value = "/related-publications")
  public ResponseEntity<?> postDomainObject(@RequestBody RelatedPublication relatedPublication) {
    return super.postDomainObject(relatedPublication);
  }

  @Override
  @PutMapping(value = "/related-publications/{id:.+}")
  public ResponseEntity<?> putDomainObject(@RequestBody RelatedPublication relatedPublication) {
    return super.putDomainObject(relatedPublication);
  }

  @Override
  @DeleteMapping("/related-publications/{id:.+}")
  public ResponseEntity<?> deleteDomainObject(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(RelatedPublication domainObject) {
    return UriComponentsBuilder.fromPath("/api/related-publications/" + domainObject.getId())
        .build().toUri();
  }
}
