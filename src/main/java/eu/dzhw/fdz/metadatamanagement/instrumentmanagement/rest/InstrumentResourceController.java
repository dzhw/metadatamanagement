package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.rest;

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
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Instrument REST Controller which overrides default spring data rest methods.
 *
 * @author René Reitmann
 */
@Controller
@Tag(name = "Instrument Resource", description = "Endpoints used by the MDM to manage instruments.")
public class InstrumentResourceController
    extends GenericDomainObjectResourceController<Instrument, CrudService<Instrument>> {

  public InstrumentResourceController(CrudService<Instrument> crudService) {
    super(crudService);
  }

  @Override
  @Operation(
      summary = "Get the instrument. Public users will get the latest version of the"
          + " instrument."
          + " If the id is postfixed with the version number it will return exactly the "
          + "requested version, if available.")
  @GetMapping(value = "/api/instruments/{id:.+}")
  @ResponseBody
  public ResponseEntity<Instrument> getDomainObject(@PathVariable String id) {
    return super.getDomainObject(id);
  }


  @Override
  @PostMapping(value = "/api/instruments")
  public ResponseEntity<?> postDomainObject(@RequestBody Instrument instrument) {
    return super.postDomainObject(instrument);
  }

  @Override
  @PutMapping(value = "/api/instruments/{id:.+}")
  public ResponseEntity<?> putDomainObject(@RequestBody Instrument instrument) {
    return super.putDomainObject(instrument);
  }

  @Override
  @DeleteMapping("/api/instruments/{id:.+}")
  public ResponseEntity<?> deleteDomainObject(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(Instrument domainObject) {
    return UriComponentsBuilder.fromPath("/api/instruments/" + domainObject.getId()).build()
        .toUri();
  }
}
