package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.mailmanagement.service.MailService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.AssigneeGroup;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ProjectReleasedEvent;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;

/**
 * Service for managing the domain object/aggregate {@link DataAcquisitionProject}.
 * 
 * @author René Reitmann
 */
@Service
@RepositoryEventHandler
public class DataAcquisitionProjectManagementService
    implements CrudService<DataAcquisitionProject> {

  private final DataAcquisitionProjectRepository acquisitionProjectRepository;

  private final UserInformationProvider userInformationProvider;

  private final DataAcquisitionProjectChangesProvider changesProvider;

  private final UserRepository userRepository;

  private final MailService mailService;

  private final MetadataManagementProperties metadataManagementProperties;

  private final DataAcquisitionProjectShadowCopyDataSource dataAcquisitionProjectShadowCopyDataSource;

  private final ShadowCopyService<DataAcquisitionProject> shadowCopyService;

  private final ShadowCopyQueueItemService shadowCopyQueueItemService;

  private final GenericShadowableDomainObjectCrudHelper<DataAcquisitionProject, DataAcquisitionProjectRepository> crudHelper;

  /**
   * Creates a new {@link DataAcquisitionProjectManagementService} instance.
   */
  public DataAcquisitionProjectManagementService(
      DataAcquisitionProjectRepository dataAcquisitionProjectRepo,
      UserInformationProvider userInformationProvider,
      DataAcquisitionProjectChangesProvider changesProvider, UserRepository userRepository,
      MailService mailService, MetadataManagementProperties metadataManagementProperties,
      DataAcquisitionProjectShadowCopyDataSource dataAcquisitionProjectShadowCopyDataSource,
      ShadowCopyService<DataAcquisitionProject> shadowCopyService,
      ShadowCopyQueueItemService shadowCopyQueueItemService,
      ApplicationEventPublisher applicationEventPublisher) {
    this.acquisitionProjectRepository = dataAcquisitionProjectRepo;
    this.userInformationProvider = userInformationProvider;
    this.changesProvider = changesProvider;
    this.userRepository = userRepository;
    this.mailService = mailService;
    this.metadataManagementProperties = metadataManagementProperties;
    this.dataAcquisitionProjectShadowCopyDataSource = dataAcquisitionProjectShadowCopyDataSource;
    this.shadowCopyService = shadowCopyService;
    this.shadowCopyQueueItemService = shadowCopyQueueItemService;
    this.crudHelper = new GenericShadowableDomainObjectCrudHelper<>(dataAcquisitionProjectRepo,
        applicationEventPublisher);
  }

  /**
   * Searches for {@link DataAcquisitionProject} items for the given id. The result may be limited
   * if the current user is not an admin or publisher.
   * 
   * @param projectId Project id
   * @return A list of {@link DataAcquisitionProject}
   */
  public List<DataAcquisitionProject> findByIdLikeOrderByIdAsc(String projectId) {
    if (isAdmin() || isPublisher()) {
      return acquisitionProjectRepository
          .findByIdLikeAndShadowIsFalseAndSuccessorIdIsNull(projectId);
    } else {
      String loginName = userInformationProvider.getUserLogin();
      return acquisitionProjectRepository
          .findAllMastersByIdLikeAndPublisherIdOrderByIdAsc(projectId, loginName);
    }
  }

  /**
   * Searches for a {@link DataAcquisitionProject} with the given id. The result depends on the
   * user's role. Publishers and administrators find everything (provided the project with the given
   * id exists). In all other cases the user must be a data provider for the requested project.
   *
   * @param projectId Project id
   * @return Optional of {@link DataAcquisitionProject}, might contain {@code null} if the project
   *         doesn't exist or if the user has insufficient access rights.
   */
  public Optional<DataAcquisitionProject> findDataAcquisitionProjectById(String projectId) {
    String loginName = userInformationProvider.getUserLogin();

    if (isAdmin() || isPublisher()) {
      return acquisitionProjectRepository.findById(projectId);
    } else {
      return acquisitionProjectRepository.findByProjectIdAndDataProviderId(projectId, loginName);
    }
  }

  private boolean isAdmin() {
    return userInformationProvider.isUserInRole(AuthoritiesConstants.ADMIN);
  }

  private boolean isPublisher() {
    return userInformationProvider.isUserInRole(AuthoritiesConstants.PUBLISHER);
  }

  @HandleBeforeSave
  void onHandleBeforeSave(DataAcquisitionProject newDataAcquisitionProject) {
    Optional<DataAcquisitionProject> oldProject =
        acquisitionProjectRepository.findById(newDataAcquisitionProject.getId());
    changesProvider.put(oldProject.orElse(null), newDataAcquisitionProject);
  }

  @HandleAfterSave
  void onHandleAfterSave(DataAcquisitionProject newDataAcquisitionProject) {
    if (!newDataAcquisitionProject.isShadow()) {
      final String projectId = newDataAcquisitionProject.getId();

      if (isProjectRelease(projectId)) {
        shadowCopyQueueItemService.createShadowCopyTask(projectId,
            newDataAcquisitionProject.getRelease().getVersion());
      }

      sendPublishersDataProvidersChangedMails(projectId);
      sendAssigneeGroupChangedMails(newDataAcquisitionProject);
    }
  }

  @EventListener
  void onProjectReleaseEvent(ProjectReleasedEvent projectReleasedEvent) {
    shadowCopyService.createShadowCopies(projectReleasedEvent.getDataAcquisitionProject(),
        projectReleasedEvent.getPreviousReleaseVersion(),
        dataAcquisitionProjectShadowCopyDataSource);
  }

  private void sendAssigneeGroupChangedMails(DataAcquisitionProject newDataAcquisitionProject) {
    String projectId = newDataAcquisitionProject.getId();
    String sender = metadataManagementProperties.getProjectmanagement().getEmail();
    if (changesProvider.hasAssigneeGroupChanged(projectId)) {
      if (isProjectForcefullyReassignedByPublisher(projectId)) {
        List<String> dataProviders = changesProvider.getOldDataAcquisitionProject(projectId)
            .getConfiguration().getDataProviders();
        List<User> users = userRepository.findAllByLoginIn(new HashSet<>(dataProviders));
        User currentUser =
            userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).orElse(null);
        mailService.sendDataProviderAccessRevokedMail(users, projectId,
            newDataAcquisitionProject.getLastAssigneeGroupMessage(), sender, currentUser);
      } else {
        AssigneeGroup assigneeGroup = changesProvider.getNewAssigneeGroup(projectId);
        Set<String> userNames;

        switch (assigneeGroup) {
          case DATA_PROVIDER:
            List<String> dataProviders =
                newDataAcquisitionProject.getConfiguration().getDataProviders();
            userNames =
                dataProviders != null ? new HashSet<>(dataProviders) : Collections.emptySet();
            break;
          case PUBLISHER:
            userNames = new HashSet<>(newDataAcquisitionProject.getConfiguration().getPublishers());
            break;
          default:
            throw new IllegalStateException("Unknown assignee group " + assigneeGroup);
        }

        if (!userNames.isEmpty()) {
          List<User> users = userRepository.findAllByLoginIn(userNames);
          User currentUser =
              userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).orElse(null);
          mailService.sendAssigneeGroupChangedMail(users, projectId,
              newDataAcquisitionProject.getLastAssigneeGroupMessage(), sender, currentUser);
        }
      }
    }
  }

  private boolean isProjectForcefullyReassignedByPublisher(String projectId) {

    DataAcquisitionProject oldProject = changesProvider.getOldDataAcquisitionProject(projectId);
    DataAcquisitionProject newProject = changesProvider.getNewDataAcquisitionProject(projectId);

    String loginName = SecurityUtils.getCurrentUserLogin();
    boolean isAssignedPublisher = oldProject.getConfiguration().getPublishers().contains(loginName);

    return isAssignedPublisher && oldProject.getAssigneeGroup() == AssigneeGroup.DATA_PROVIDER
        && newProject.getAssigneeGroup() == AssigneeGroup.PUBLISHER;
  }

  private void sendPublishersDataProvidersChangedMails(String projectId) {
    List<String> addedPublishers = changesProvider.getAddedPublisherUserNamesList(projectId);
    List<String> removedPublishers = changesProvider.getRemovedPublisherUserNamesList(projectId);

    List<String> addedDataProviders = changesProvider.getAddedDataProviderUserNamesList(projectId);
    List<String> removedDataProviders =
        changesProvider.getRemovedDataProviderUserNamesList(projectId);

    UserFetchResult users = fetchUsersForUserNames(addedPublishers, removedPublishers,
        addedDataProviders, removedDataProviders);

    String sender = metadataManagementProperties.getProjectmanagement().getEmail();

    mailService.sendPublishersAddedMail(users.addedPublisherUsers, projectId, sender);
    mailService.sendPublisherRemovedMail(users.removedPublisherUsers, projectId, sender);
    mailService.sendDataProviderAddedMail(users.addedDataProviderUsers, projectId, sender);
    mailService.sendDataProviderRemovedMail(users.removedDataProviderUsers, projectId, sender);

  }

  private UserFetchResult fetchUsersForUserNames(List<String> addedPublishers,
      List<String> removedPublishers, List<String> addedDataProviders,
      List<String> removedDataProviders) {
    Set<String> userLoginNames = new HashSet<>(addedPublishers);
    userLoginNames.addAll(removedPublishers);
    userLoginNames.addAll(addedDataProviders);
    userLoginNames.addAll(removedDataProviders);

    List<User> users = userRepository.findAllByLoginIn(userLoginNames);

    List<User> addedPublisherUsers = filterUsersByUserNames(users, addedPublishers);
    List<User> removedPublisherUsers = filterUsersByUserNames(users, removedPublishers);
    List<User> addedDataProviderUsers = filterUsersByUserNames(users, addedDataProviders);
    List<User> removedDataProviderUsers = filterUsersByUserNames(users, removedDataProviders);

    return new UserFetchResult(addedPublisherUsers, removedPublisherUsers, addedDataProviderUsers,
        removedDataProviderUsers);
  }

  private List<User> filterUsersByUserNames(List<User> users, List<String> userNames) {
    return users.stream().filter(u -> userNames.contains(u.getLogin()))
        .collect(Collectors.toList());
  }

  /**
   * Encapsulates repository query result for added or removed publishers and data providers.
   */
  private static class UserFetchResult {
    private List<User> addedPublisherUsers;
    private List<User> removedPublisherUsers;
    private List<User> addedDataProviderUsers;
    private List<User> removedDataProviderUsers;

    UserFetchResult(List<User> addedPublisherUsers, List<User> removedPublisherUsers,
        List<User> addedDataProviderUsers, List<User> removedDataProviderUsers) {
      this.addedPublisherUsers = addedPublisherUsers;
      this.removedPublisherUsers = removedPublisherUsers;
      this.addedDataProviderUsers = addedDataProviderUsers;
      this.removedDataProviderUsers = removedDataProviderUsers;
    }
  }

  private boolean isProjectRelease(String dataAcquisitionProjectId) {
    DataAcquisitionProject oldProject =
        changesProvider.getOldDataAcquisitionProject(dataAcquisitionProjectId);

    DataAcquisitionProject newProject =
        changesProvider.getNewDataAcquisitionProject(dataAcquisitionProjectId);

    if (oldProject != null && newProject != null) {
      Release oldRelease = oldProject.getRelease();
      Release newRelease = newProject.getRelease();

      if (oldRelease == null && newRelease != null) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  @Override
  public Optional<DataAcquisitionProject> read(String id) {
    return crudHelper.read(id);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.ADMIN})
  public void delete(DataAcquisitionProject project) {
    if (!project.getHasBeenReleasedBefore()) {
      crudHelper.deleteMaster(project, true);
    } else {
      throw new IllegalStateException(
          "Project has been released before and therefore it must not be deleted.");
    }
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.ADMIN})
  public DataAcquisitionProject save(DataAcquisitionProject project) {
    return crudHelper.saveMaster(project, true);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.ADMIN})
  public DataAcquisitionProject create(DataAcquisitionProject domainObject) {
    return crudHelper.createMaster(domainObject, true);
  }
}
