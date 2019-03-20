package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.rest;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericShadowableDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
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
 * Instrument REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
public class InstrumentResourceController 
    extends GenericShadowableDomainObjectResourceController<Instrument, InstrumentRepository> {

  @Autowired
  public InstrumentResourceController(InstrumentRepository instrumentRepository,
                                      ApplicationEventPublisher applicationEventPublisher) {
    super(instrumentRepository, applicationEventPublisher);
  }

  /**
   * Override default get by id since it does not set cache headers correctly.
   * 
   * @param id a Instrument id
   * @return the Instrument or not found
   */
  @RequestMapping(method = RequestMethod.GET, value = "/instruments/{id:.+}")
  public ResponseEntity<Instrument> findInstrument(@PathVariable String id) {
    return super.findDomainObject(id);
  }

  /**
   * Override default put to prevent updates on shadow copies or creating new ones.
   * @param id Intrument id
   * @param instrument Instrument data to create or update
   */
  @RequestMapping(method = RequestMethod.PUT, value = "/instruments/{id:.+}")
  public ResponseEntity<?> putInstrument(@PathVariable  String id,
                                         @Valid @RequestBody Instrument instrument) {
    return super.putDomainObject(id, instrument);
  }

  /**
   * Override default post to prevent creating new shadow copies.
   * @param instrument Instrument to create
   */
  @RequestMapping(method = RequestMethod.POST, value = "/instruments")
  public ResponseEntity<?> postInstrument(@Valid @RequestBody Instrument instrument) {
    return super.postDomainObject(instrument);
  }

  /**
   * Override default delete to prevent clients from deleting shadow copies.
   * @param id Instrument id
   */
  @RequestMapping(method = RequestMethod.DELETE, value = "/instruments/{id:.+}")
  public ResponseEntity<?> deleteInstrument(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(Instrument domainObject) {
    return UriComponentsBuilder.fromPath("/api/instruments/" + domainObject.getId()).build()
        .toUri();
  }
}
