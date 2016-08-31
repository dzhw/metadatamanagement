package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;

/**
 * Service for creating and updating variable. Used for updating variables in mongo and
 * elasticsearch.
 * 
 * @author Daniel Katzberg
 */
@Service
@RepositoryEventHandler
public class StudyService {

  @Inject
  private StudyRepository studyRepository;
  
  @Inject
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  /**
   * Delete all studies when the dataAcquisitionProject was deleted.
   * 
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteAllStudiesByProjectId(dataAcquisitionProject.getId());
  }
  
  /**
   * A service method for deletion of studies within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   * @return List of deleted studies
   */
  public List<Study> deleteAllStudiesByProjectId(String dataAcquisitionProjectId) {
    List<Study> deletedStudies =
        this.studyRepository.deleteByDataAcquisitionProjectId(dataAcquisitionProjectId);
    deletedStudies.forEach(study -> {
      elasticsearchUpdateQueueService.enqueue(
          study.getId(), 
          ElasticsearchType.studies, 
          ElasticsearchUpdateQueueAction.DELETE);      
    });
    return deletedStudies;
  }  
}
