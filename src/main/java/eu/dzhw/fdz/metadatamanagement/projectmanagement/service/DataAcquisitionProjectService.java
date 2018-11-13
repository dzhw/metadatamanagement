package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.data.rest.core.event.BeforeDeleteEvent;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;

import java.util.List;

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
   * Deletes a Data Acquisition Project, it it hasn't been released before.
   * @param dataAcquisitionProject A representation of the Data Acquisition Project.
   * @return True = The Project is deleted. False = The Project is not deleted.
   */
  public boolean deleteDataAcquisitionProject(DataAcquisitionProject dataAcquisitionProject) {

    //just delete project, if it has not been released before.
    if (!dataAcquisitionProject.getHasBeenReleasedBefore()) {
      this.eventPublisher.publishEvent(new BeforeDeleteEvent(dataAcquisitionProject));
      this.acquisitionProjectRepository.delete(dataAcquisitionProject);
      this.eventPublisher.publishEvent(new AfterDeleteEvent(dataAcquisitionProject));
    }

    return !dataAcquisitionProject.getHasBeenReleasedBefore();
  }

    public List<DataAcquisitionProject> findDataAcquisitionProjectListById(String id) {
        return acquisitionProjectRepository.findByIdLikeOrderByIdAsc(id);
    }
}
