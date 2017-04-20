package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;

/**
 * Service class for the Data Acquisition Project. Handles calls to the mongo db.
 * @author Daniel Katzberg
 *
 */
@Service
public class DataAcquisitionProjectService {

  @Autowired
  private DataAcquisitionProjectRepository acquisitionProjectRepository;
  
  @Autowired
  private ApplicationEventPublisher eventPublisher;
  
  /**
   * Returns a Data Acquisition Project by id.
   * @param id the id of the Data Acquisition Project.
   * @return The Data Acquisition Project object from Mongo DB. 
   */
  public DataAcquisitionProject findOne(String id) {
    return this.acquisitionProjectRepository.findOne(id);
  }
  
  /**
   * Deletes a Data Acquisition Project, it it hasn't been released before.
   * @param dataAcquisitionProject A representation of the Data Acquisition Project.
   * @return True = The Project is deleted. False = The Project is not deleted. 
   */
  public boolean deleteDataAcquisitionProject(DataAcquisitionProject dataAcquisitionProject) {
    
    //just delete project, if it has not been released before.
    if (!dataAcquisitionProject.isHasBeenReleasedBefore()) {
      this.acquisitionProjectRepository.delete(dataAcquisitionProject);
      this.eventPublisher.publishEvent(new AfterDeleteEvent(dataAcquisitionProject));
    }   
    
    return !dataAcquisitionProject.isHasBeenReleasedBefore();
  }
}
