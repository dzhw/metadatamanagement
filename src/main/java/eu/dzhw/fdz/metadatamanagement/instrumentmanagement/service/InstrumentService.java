package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;

/**
 * The service for the instruments. This service handels delete events.
 * 
 * @author Daniel Katzberg
 *
 */
@Service
@RepositoryEventHandler
public class InstrumentService {

  @Inject
  private InstrumentRepository instrumentRepository;

  @Inject
  private ApplicationEventPublisher eventPublisher;

  /**
   * Listener, which will be activate by a deletion of a data acquisition project.
   * 
   * @param dataAcquisitionProject A reference to the data acquisition project.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    List<Instrument> deletedInstruments =
        instrumentRepository.deleteByDataAcquisitionProjectId(dataAcquisitionProject.getId());
    deletedInstruments
      .forEach(instrument -> eventPublisher.publishEvent(new AfterDeleteEvent(instrument)));
  }
}
