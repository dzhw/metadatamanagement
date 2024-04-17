package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.zafarkhaja.semver.Version;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.mailmanagement.service.MailService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.AssigneeGroup;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowHidingNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowUnhidingNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.helper.DataAcquisitionProjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.Authority;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing the domain object/aggregate {@link DataAcquisitionProject}.
 *
 * @author René Reitmann
 */
@Service
@RepositoryEventHandler
@RequiredArgsConstructor
public class DataAcquisitionProjectManagementService
    implements CrudService<DataAcquisitionProject> {

  private final DataAcquisitionProjectRepository acquisitionProjectRepository;

  private final UserInformationProvider userInformationProvider;

  private final DataAcquisitionProjectChangesProvider changesProvider;

  private final UserRepository userRepository;

  private final MailService mailService;

  private final MetadataManagementProperties metadataManagementProperties;

  private final ShadowCopyQueueItemService shadowCopyQueueItemService;

  private final DataAcquisitionProjectCrudHelper crudHelper;

  private final DataAcquisitionProjectVersionsService projectVersionsService;

  private final DaraService daraService;

  private final Environment environment;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  /**
   * Searches for {@link DataAcquisitionProject} items for the given id. The result may be limited
   * if the current user is not an admin or publisher.
   *
   * @param projectId Project id
   * @return A list of {@link DataAcquisitionProject}
   */
  public Page<DataAcquisitionProject> findByIdLikeOrderByIdAsc(String projectId, Pageable pageable) {
    if (isAdmin() || isPublisher()) {
      return acquisitionProjectRepository
        .findByIdLikeAndShadowIsFalseAndSuccessorIdIsNullOrderByIdAsc(projectId, pageable);
    } else {
      String loginName = userInformationProvider.getUserLogin();
      return acquisitionProjectRepository
        .findAllMastersByIdLikeAndPublisherIdOrderByIdAsc(projectId, loginName, pageable);
    }
  }

  private boolean isAdmin() {
    return userInformationProvider.isUserInRole(AuthoritiesConstants.ADMIN);
  }

  private boolean isPublisher() {
    return userInformationProvider.isUserInRole(AuthoritiesConstants.PUBLISHER);
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

  private boolean isProjectBeingReleased(DataAcquisitionProject oldProject,
      DataAcquisitionProject newProject) {
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
    if (!project.getHasBeenReleasedBefore() || this.isAdmin()
        && environment.acceptsProfiles(Profiles.of("!" + Constants.SPRING_PROFILE_PROD))) {
      crudHelper.deleteMaster(project);
      if (project.getHasBeenReleasedBefore()) {
        // delete all shadows
        acquisitionProjectRepository.findByMasterIdAndShadowIsTrue(project.getMasterId())
            .forEach(shadow -> {
              shadowCopyQueueItemService.scheduleShadowCopyDeletion(shadow.getMasterId(),
                  shadow.getRelease());
            });
      }
    } else {
      throw new IllegalStateException(
          "Project has been released before and therefore it must not be deleted.");
    }
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.ADMIN})
  public DataAcquisitionProject save(DataAcquisitionProject project) {
    DataAcquisitionProject oldProject = crudHelper.read(project.getId()).orElse(null);
    if (isProjectBeingReleased(oldProject, project)) {
      Optional<DataAcquisitionProject> previousRelease = acquisitionProjectRepository
          .findById(project.getMasterId() + "-" + project.getRelease().getVersion());
      if (previousRelease.isPresent()) {
        project.getRelease().setFirstDate(previousRelease.get().getRelease().getFirstDate());
      } else {
        project.getRelease().setFirstDate(LocalDateTime.now());
      }
    }
    project = crudHelper.saveMaster(project);

    final String projectId = project.getId();

    if (isProjectBeingReleased(oldProject, project)) {
      shadowCopyQueueItemService.scheduleShadowCopyCreation(projectId, project.getRelease());
    } else {
      sendPublishersDataProvidersChangedMails(projectId);
      sendAssigneeGroupChangedMails(project);
    }
    return project;
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.ADMIN})
  public DataAcquisitionProject create(DataAcquisitionProject domainObject) {
    return crudHelper.createMaster(domainObject);
  }

  @Override
  public Optional<DataAcquisitionProject> readSearchDocument(String id) {
    return crudHelper.readSearchDocument(id);
  }

  /**
   * Notify all {@link AuthoritiesConstants.RELEASE_MANAGER}'s about the new major release.
   *
   * @param shadowCopyingEndedEvent Emitted by {@link ShadowCopyQueueItemService}.
   * @throws TemplateException thrown if template processing for dara's xml fails
   * @throws IOException thrown if IO errors occur during template processing
   */
  @EventListener
  public void onShadowCopyingEnded(ShadowCopyingEndedEvent shadowCopyingEndedEvent)
      throws IOException, TemplateException {
    switch (shadowCopyingEndedEvent.getAction()) {
      case CREATE:
        if (shadowCopyingEndedEvent.isRerelease()) {
          // do not send mails for rereleases
          return;
        }
        Version currentVersion = Version.valueOf(shadowCopyingEndedEvent.getRelease().getVersion());
        Release previousRelease = projectVersionsService.findPreviousRelease(
            shadowCopyingEndedEvent.getDataAcquisitionProjectId(),
            shadowCopyingEndedEvent.getRelease());
        if (previousRelease == null && currentVersion.getMajorVersion() > 0
            || previousRelease != null && currentVersion.getMajorVersion() > Version
                .valueOf(previousRelease.getVersion()).getMajorVersion()) {
          // a new major release has been shadow copied
          List<User> releaseManagers = userRepository
              .findAllByAuthoritiesContaining(new Authority(AuthoritiesConstants.RELEASE_MANAGER));
          mailService.sendMailOnNewMajorProjectRelease(releaseManagers,
              shadowCopyingEndedEvent.getDataAcquisitionProjectId(),
              shadowCopyingEndedEvent.getRelease());
        }
        break;
      case HIDE:
      case UNHIDE:
        if (Version.valueOf(shadowCopyingEndedEvent.getRelease().getVersion())
            .greaterThanOrEqualTo(Version.valueOf("1.0.0"))) {
          daraService
              .registerOrUpdateProjectToDara(shadowCopyingEndedEvent.getDataAcquisitionProjectId()
                  + "-" + shadowCopyingEndedEvent.getRelease().getVersion());
        }
        break;
      case DELETE:
        break; // nothing to do in this case
      default:
        throw new IllegalArgumentException(
            shadowCopyingEndedEvent.getAction() + " has not been implemented yet!");
    }
  }

  /**
   * Load a page containing all shadow copies of the given master.
   *
   * @param masterId project id of the master
   * @param pageable pageable for paging and sorting
   * @return all shadows of the given master, may be empty
   */
  public Page<DataAcquisitionProject> findAllShadows(String masterId, Pageable pageable) {
    return crudHelper.findAllShadows(masterId, pageable);
  }

  /**
   * Hide the given shadow copy of a project.
   *
   * @param shadowProject The shadow to be hidden.
   * @throws ShadowHidingNotAllowedException thrown if the given project cannot be hidden
   */
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.ADMIN})
  public void hideShadows(DataAcquisitionProject shadowProject)
      throws ShadowHidingNotAllowedException {
    if (!shadowProject.isShadow()) {
      throw new ShadowHidingNotAllowedException("Master projects cannot be hidden!");
    }
    if (StringUtils.isEmpty(shadowProject.getSuccessorId())) {
      throw new ShadowHidingNotAllowedException("Shadows without successor cannot be hidden!");
    }
    if (shadowProject.isHidden()) {
      throw new ShadowHidingNotAllowedException("Project is already hidden!");
    }
    shadowCopyQueueItemService.scheduleShadowCopyHiding(shadowProject.getMasterId(),
        shadowProject.getRelease());
  }

  /**
   * Unhide the given shadow, thus make it visible for public users.
   *
   * @param shadowProject The shadow copy of a project.
   * @throws ShadowUnhidingNotAllowedException Thrown if the project is already unhidden.
   */
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.ADMIN})
  public void unhideShadows(DataAcquisitionProject shadowProject)
      throws ShadowUnhidingNotAllowedException {
    if (!shadowProject.isHidden()) {
      throw new ShadowUnhidingNotAllowedException("Project shadow is already unhidden!");
    }
    shadowCopyQueueItemService.scheduleShadowCopyUnhiding(shadowProject.getMasterId(),
        shadowProject.getRelease());
  }

  /**
   * Enqueue update of dataAcquisitionProject search documents when the {@link DataPackage} is changed.
   *
   * @param dataPackage the updated or created {@link DataPackage}.
   */
  @HandleAfterCreate
  @HandleAfterSave
  public void onDataPackageChanged(DataPackage dataPackage) {
    elasticsearchUpdateQueueService.enqueueUpsertAsync(
        () -> acquisitionProjectRepository.findOneIdAndVersionById(dataPackage.getDataAcquisitionProjectId()),
        ElasticsearchType.data_acquisition_projects);
  }
}
