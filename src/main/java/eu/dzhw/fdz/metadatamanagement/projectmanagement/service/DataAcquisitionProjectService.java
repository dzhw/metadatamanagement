package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserAuthenticationProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.data.rest.core.event.BeforeDeleteEvent;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for the Data Acquisition Project. Handles calls to the mongo db.
 *
 * @author Daniel Katzberg
 */
@Service
public class DataAcquisitionProjectService {

  private DataAcquisitionProjectRepository acquisitionProjectRepository;

  private ApplicationEventPublisher eventPublisher;

  private UserAuthenticationProvider userAuthenticationProvider;

  public DataAcquisitionProjectService(DataAcquisitionProjectRepository dataAcquisitionProjectRepository, ApplicationEventPublisher applicationEventPublisher, UserAuthenticationProvider userAuthenticationProvider) {
    this.acquisitionProjectRepository = dataAcquisitionProjectRepository;
    this.eventPublisher = applicationEventPublisher;
    this.userAuthenticationProvider = userAuthenticationProvider;
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
   * Searches for {@link DataAcquisitionProject} items for the given id. The result may be limited if
   * the current user is not an admin or publisher.
   *
   * @param projectId Project id
   * @return A list of {@link DataAcquisitionProject}
   */
  public List<DataAcquisitionProject> findDataAcquisitionProjectListById(String projectId) {
    UserDetails userDetails = userAuthenticationProvider.getUserDetails();

    if (isAdmin(userDetails) || isPublisher(userDetails)) {
      return acquisitionProjectRepository.findByIdLikeOrderByIdAsc(projectId);
    } else {
      return acquisitionProjectRepository.findAllByIdLikeAndPublisherId(projectId, userDetails.getUsername());
    }
  }

  private boolean isAdmin(UserDetails userDetails) {
    SimpleGrantedAuthority publisherRole = new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN);
    return userDetails.getAuthorities().contains(publisherRole);
  }

  private boolean isPublisher(UserDetails userDetails) {
    SimpleGrantedAuthority dataProviderRole = new SimpleGrantedAuthority(AuthoritiesConstants.PUBLISHER);
    return userDetails.getAuthorities().contains(dataProviderRole);
  }
}
