package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

import javax.annotation.PostConstruct;

import org.javers.core.Javers;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.common.service.GenericDomainObjectVersionsService;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;

/**
 * Service responsible for retrieving an initializing the instrument history.
 * 
 * @author René Reitmann
 */
@Service
public class InstrumentVersionsService
    extends GenericDomainObjectVersionsService<Instrument, InstrumentRepository> {
  /**
   * Construct the service.
   */
  public InstrumentVersionsService(Javers javers, InstrumentRepository instrumentRepository,
      MetadataManagementProperties metadataManagementProperties) {
    super(Instrument.class, javers, instrumentRepository, metadataManagementProperties);
  }

  /**
   * Init Javers with all current dataPackages if there are no dataPackage commits in Javers yet.
   */
  @PostConstruct
  public void initJaversForInstruments() {
    super.initJaversWithCurrentVersions();
  }
}
