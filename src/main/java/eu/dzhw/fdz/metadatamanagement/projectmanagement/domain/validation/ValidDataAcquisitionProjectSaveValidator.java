package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.AssigneeGroup;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Requirements;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

/**
 * Validate {@link DataAcquisitionProject} save attempt.
 */
@RequiredArgsConstructor
public class ValidDataAcquisitionProjectSaveValidator
    implements ConstraintValidator<ValidDataAcquisitionProjectSave, DataAcquisitionProject> {

  private static final String INVALID_ASSIGNEE_GROUP = "data-acquisition-project-"
      + "management.error.data-acquisition-project.assignee-group.not-assigned";
  private static final String MISSING_ASSIGNEE_GROUP_MESSAGE = "data-acquisition-project-"
      + "management.error.data-acquisition-project.last-assignee-group-message.not-empty";
  private static final String INVALID_PUBLISHER_UPDATE = "data-acquisition-project-"
      + "management.error.configuration.publishers.unauthorized";
  private static final String INVALID_DATA_PROVIDER_UPDATE = "data-acquisition-project-"
      + "management.error.configuration.data-providers.update-not-allowed";
  private static final String INVALID_REQUIREMENTS_UPDATE = "data-acquisition-project-"
      + "management.error.configuration.requirements.unauthorized";
  private static final String CREATE_PROJECT_NOT_ALLOWED = "data-acquisition-project-"
      + "management.error.create.unauthorized";

  private final DataAcquisitionProjectRepository repository;

  @Override
  public void initialize(ValidDataAcquisitionProjectSave constraintAnnotation) {
  }

  @Override
  public boolean isValid(DataAcquisitionProject dataAcquisitionProject,
                         ConstraintValidatorContext constraintValidatorContext) {

    constraintValidatorContext.disableDefaultConstraintViolation();

    final String id = dataAcquisitionProject.getId();
    Optional<DataAcquisitionProject> oldDataProjectOpt = repository.findById(id);
    if (oldDataProjectOpt.isPresent()) {
      DataAcquisitionProject oldProject = oldDataProjectOpt.get();

      if (!isAssigneeGroupChangePermitted(oldProject, dataAcquisitionProject)) {
        constraintValidatorContext
            .buildConstraintViolationWithTemplate(INVALID_ASSIGNEE_GROUP)
            .addConstraintViolation();
        return false;
      }

      if (!isMessageToAssigneeGroupProvided(oldProject, dataAcquisitionProject)) {
        constraintValidatorContext
            .buildConstraintViolationWithTemplate(MISSING_ASSIGNEE_GROUP_MESSAGE)
            .addConstraintViolation();
        return false;
      }

      if (!isPublisherUpdatePermitted(oldProject, dataAcquisitionProject)) {
        constraintValidatorContext
            .buildConstraintViolationWithTemplate(INVALID_PUBLISHER_UPDATE)
            .addConstraintViolation();
        return false;
      }

      if (!isDataProviderUpdatePermitted(oldProject, dataAcquisitionProject)) {
        constraintValidatorContext
            .buildConstraintViolationWithTemplate(INVALID_DATA_PROVIDER_UPDATE)
            .addConstraintViolation();
        return false;
      }

      if (!isProjectRequirementsUpdatePermitted(oldProject, dataAcquisitionProject)) {
        constraintValidatorContext
            .buildConstraintViolationWithTemplate(INVALID_REQUIREMENTS_UPDATE)
            .addConstraintViolation();
        return false;
      }

      return true;
    } else {
      if (!isDataAcquisitionProjectCreatePermitted()) {
        constraintValidatorContext
            .buildConstraintViolationWithTemplate(CREATE_PROJECT_NOT_ALLOWED)
            .addConstraintViolation();
        return false;
      } else {
        return true;
      }
    }
  }

  /*
   * Only admins and publishers are allowed to modify the publisher list of a project.
   */
  private boolean isPublisherUpdatePermitted(DataAcquisitionProject oldProject,
                                             DataAcquisitionProject newProject) {

    List<String> oldPublishers = oldProject.getConfiguration().getPublishers();
    List<String> newPublishers = newProject.getConfiguration().getPublishers();

    return isNotModified(oldPublishers, newPublishers)
        || SecurityUtils.isUserInRole(AuthoritiesConstants.PUBLISHER)
        || SecurityUtils.isUserInRole(AuthoritiesConstants.ADMIN);
  }

  /*
   * If the project configuration contained at least one data provider, it must also contain at
   * least one data provider after the update.
   */
  private boolean isDataProviderUpdatePermitted(DataAcquisitionProject oldProject,
                                                DataAcquisitionProject newProject) {

    List<String> oldDataProviders = oldProject.getConfiguration().getDataProviders();
    List<String> newDataProviders = newProject.getConfiguration().getDataProviders();

    return oldDataProviders.isEmpty() || !newDataProviders.isEmpty();
  }

  /*
   * Requirement updates are only permitted if the user is a publisher of the project.
   */
  private boolean isProjectRequirementsUpdatePermitted(DataAcquisitionProject oldProject,
                                                       DataAcquisitionProject newProject) {

    Requirements oldRequirements = oldProject.getConfiguration().getRequirements();
    Requirements newRequirements = newProject.getConfiguration().getRequirements();

    return isNotModified(oldRequirements, newRequirements) || oldProject.getConfiguration()
        .getPublishers().contains(SecurityUtils.getCurrentUserLogin());
  }

  /*
   * Only publishers are permitted to create new projects.
   */
  private boolean isDataAcquisitionProjectCreatePermitted() {
    return SecurityUtils.isUserInRole(AuthoritiesConstants.PUBLISHER);
  }

  /**
   * Project assignment can only be changed if user is publisher of the project, otherwise the user
   * must be a data provider and the project must currently be assigned to data providers.
   */
  private boolean isAssigneeGroupChangePermitted(DataAcquisitionProject oldProject,
                                                 DataAcquisitionProject newProject) {

    if (oldProject.getAssigneeGroup() == newProject.getAssigneeGroup()) {
      return true;
    }

    String userLogin = SecurityUtils.getCurrentUserLogin();

    if (oldProject.getConfiguration().getPublishers().contains(userLogin)) {
      return true;
    }

    return oldProject.getAssigneeGroup() == AssigneeGroup.DATA_PROVIDER && oldProject
        .getConfiguration().getDataProviders().contains(userLogin);
  }

  /**
   * Current assignee group must provide a message if the group assignment changes.
   */
  private boolean isMessageToAssigneeGroupProvided(DataAcquisitionProject oldProject,
                                                   DataAcquisitionProject dataAcquisitionProject) {
    if (oldProject == null) {
      return true;
    }

    if (oldProject.getAssigneeGroup() == dataAcquisitionProject.getAssigneeGroup()) {
      return true;
    }

    boolean isAssignedPublisher = oldProject.getConfiguration().getPublishers()
        .contains(SecurityUtils.getCurrentUserLogin());
    boolean isProjectAssignedToDataProvider = oldProject
        .getAssigneeGroup() == AssigneeGroup.DATA_PROVIDER;
    boolean isNextGroupPublisher = dataAcquisitionProject
        .getAssigneeGroup() == AssigneeGroup.PUBLISHER;

    // If we are an assigned publisher and we reassign the project to publishers, ignore the message
    if (isAssignedPublisher && isProjectAssignedToDataProvider && isNextGroupPublisher) {
      return true;
    } else {
      return StringUtils.hasText(dataAcquisitionProject.getLastAssigneeGroupMessage());
    }
  }

  private boolean isNotModified(Object objA, Object objB) {
    return Objects.equals(objA, objB);
  }
}
