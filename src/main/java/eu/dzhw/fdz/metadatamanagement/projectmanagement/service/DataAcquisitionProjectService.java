package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.data.rest.core.event.BeforeDeleteEvent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for the Data Acquisition Project. Handles calls to the mongo db.
 *
 * @author Daniel Katzberg
 */
@Service
public class DataAcquisitionProjectService {

  private DataAcquisitionProjectRepository acquisitionProjectRepository;

  private ApplicationEventPublisher eventPublisher;

  private UserInformationProvider userInformationProvider;

  /**
   * Creates a new {@link DataAcquisitionProjectService} instance.
   */
  public DataAcquisitionProjectService(DataAcquisitionProjectRepository dataAcquisitionProjectRepo,
                                       ApplicationEventPublisher applicationEventPublisher,
                                       UserInformationProvider userInformationProvider) {
    this.acquisitionProjectRepository = dataAcquisitionProjectRepo;
    this.eventPublisher = applicationEventPublisher;
    this.userInformationProvider = userInformationProvider;
  }

  /**
   * Deletes a Data Acquisition Project, it it hasn't been released before.
   *
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

  /**
   * Searches for {@link DataAcquisitionProject} items for the given id. The result
   * may be limited if the current user is not an admin or publisher.
   *
   * @param projectId Project id
   * @return A list of {@link DataAcquisitionProject}
   */
  public List<DataAcquisitionProject> findDataAcquisitionProjectListById(String projectId) {
    String loginName = userInformationProvider.getUserLogin();

    if (isAdmin() || isPublisher()) {
      return acquisitionProjectRepository.findByIdLikeOrderByIdAsc(projectId);
    } else {
      return acquisitionProjectRepository.findAllByIdLikeAndPublisherId(
        projectId,
        loginName
      );
    }
  }

  /**
   * Searches for a {@link DataAcquisitionProject} with the given id. The result depends on the
   * user's role. Publishers and administrators find everything (provided the project with the given
   * id exists). In all other cases the user must be a data provider for the requested project.
   *
   * @param projectId Project id
   * @return Optional of {@link DataAcquisitionProject}, might contain {@code null} if the project
   *     doesn't exist or if the user has insufficient access rights.
   */
  public Optional<DataAcquisitionProject> findDataAcquisitionProjectById(String projectId) {
    String loginName = userInformationProvider.getUserLogin();

    if (isAdmin() || isPublisher()) {
      return acquisitionProjectRepository.findById(projectId);
    } else {
      return acquisitionProjectRepository.findByProjectIdAndDataProviderId(
        projectId,
        loginName
      );
    }
  }

  private boolean isAdmin() {
    return userInformationProvider.isUserInRole(AuthoritiesConstants.ADMIN);
  }

  private boolean isPublisher() {
    return userInformationProvider.isUserInRole(AuthoritiesConstants.PUBLISHER);
  }
}
