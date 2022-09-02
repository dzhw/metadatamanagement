package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.rest;

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
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * RelatedPublication REST Controller which overrides default spring data rest methods.
 *
 * @author René Reitmann
 */
@Controller
@Tag(name = "Publication Resource",
    description = "Endpoints used by the MDM to manage publications.")
public class RelatedPublicationResourceController extends
    GenericDomainObjectResourceController<RelatedPublication, CrudService<RelatedPublication>> {

  public RelatedPublicationResourceController(CrudService<RelatedPublication> crudService) {
    super(crudService);
  }

  @Override
  @Operation(summary = "Get the publication.")
  @GetMapping(value = "/api/related-publications/{id:.+}")
  @ResponseBody
  public ResponseEntity<RelatedPublication> getDomainObject(@PathVariable String id) {
    return super.getDomainObject(id);
  }


  @Override
  @PostMapping(value = "/api/related-publications")
  public ResponseEntity<?> postDomainObject(@RequestBody RelatedPublication relatedPublication) {
    return super.postDomainObject(relatedPublication);
  }

  @Override
  @PutMapping(value = "/api/related-publications/{id:.+}")
  public ResponseEntity<?> putDomainObject(@RequestBody RelatedPublication relatedPublication) {
    return super.putDomainObject(relatedPublication);
  }

  @Override
  @DeleteMapping("/api/related-publications/{id:.+}")
  public ResponseEntity<?> deleteDomainObject(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(RelatedPublication domainObject) {
    return UriComponentsBuilder.fromPath("/api/related-publications/" + domainObject.getId())
        .build().toUri();
  }
}
