package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

/**
 * The service for the instruments. This service handels delete events.
 * 
 * @author René Reitmann
 *
 */
@Service
@RepositoryEventHandler
public class InstrumentService {

  @Autowired
  private InstrumentRepository instrumentRepository;
  
  @Autowired 
  private InstrumentAttachmentService instrumentAttachmentService;

  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  /**
   * Delete all instruments when the dataAcquisitionProject was deleted.
   * 
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteAllInstrumentsByProjectId(dataAcquisitionProject.getId());
  }
  
  /**
   * A service method for deletion of instruments within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  public void deleteAllInstrumentsByProjectId(String dataAcquisitionProjectId) {
    List<Instrument> deletedInstruments =
        instrumentRepository.deleteByDataAcquisitionProjectId(dataAcquisitionProjectId);
    deletedInstruments.forEach(instrument -> {
      instrumentAttachmentService.deleteAllByInstrument(instrument.getId());
      elasticsearchUpdateQueueService.enqueue(
          instrument.getId(), 
          ElasticsearchType.instruments, 
          ElasticsearchUpdateQueueAction.DELETE);      
    });
  }
  
  /**
   * Enqueue deletion of instrument search document when the instrument is deleted.
   * 
   * @param instrument the deleted instrument.
   */
  @HandleAfterDelete
  public void onInstrumentDeleted(Instrument instrument) {
    instrumentAttachmentService.deleteAllByInstrument(instrument.getId());
    elasticsearchUpdateQueueService.enqueue(
        instrument.getId(), 
        ElasticsearchType.instruments, 
        ElasticsearchUpdateQueueAction.DELETE);
  }
  
  /**
   * Enqueue update of instrument search document when the instrument is updated.
   * 
   * @param instrument the updated or created instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  public void onInstrumentSaved(Instrument instrument) {
    elasticsearchUpdateQueueService.enqueue(
        instrument.getId(), 
        ElasticsearchType.instruments, 
        ElasticsearchUpdateQueueAction.UPSERT);
  }
}
