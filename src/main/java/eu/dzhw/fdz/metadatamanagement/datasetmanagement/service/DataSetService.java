package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;

/**
 * This service for {@link DataSet} will wait for delete events of a survey or a data acquisition
 * project.
 * 
 * @author Daniel Katzberg
 *
 */
@Service
@RepositoryEventHandler
public class DataSetService {

  @Inject
  private DataSetRepository dataSetRepository;

  @Inject
  private ApplicationEventPublisher eventPublisher;

  /**
   * Delete all data sets when the dataAcquisitionProject was deleted.
   * 
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteDataSetsByProjectId(dataAcquisitionProject.getId());
  }
  
  /**
   * A service method for deletion of dataSets within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   * @return List of deleted dataSets
   */
  public List<DataSet> deleteDataSetsByProjectId(String dataAcquisitionProjectId) {
    List<DataSet> deletedDataSets =
        this.dataSetRepository.deleteByDataAcquisitionProjectId(dataAcquisitionProjectId);
    deletedDataSets.forEach(dataSet -> eventPublisher.publishEvent(new AfterDeleteEvent(dataSet)));
    return deletedDataSets;
  }
}
