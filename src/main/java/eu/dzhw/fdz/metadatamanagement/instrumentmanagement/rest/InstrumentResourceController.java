package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;

/**
 * Instrument REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
public class InstrumentResourceController 
    extends GenericDomainObjectResourceController<Instrument, InstrumentRepository> {

  @Autowired
  public InstrumentResourceController(InstrumentRepository instrumentRepository) {
    super(instrumentRepository);
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
}
