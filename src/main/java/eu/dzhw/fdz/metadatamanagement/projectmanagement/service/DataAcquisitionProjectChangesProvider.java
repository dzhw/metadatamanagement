package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import eu.dzhw.fdz.metadatamanagement.common.service.DomainObjectChangesProvider;
import eu.dzhw.fdz.metadatamanagement.common.service.util.ListUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.AssigneeGroup;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;

/**
 * Remember the previous version of a related data acquisition project per request.
 */
@Component
@RequestScope
public class DataAcquisitionProjectChangesProvider
    extends DomainObjectChangesProvider<DataAcquisitionProject> {
  /**
   * Get user names of added publishers.
   * 
   * @param projectId id of project where publishers additions should be checked
   * @return list of added publisher user names
   */
  public List<String> getAddedPublisherUserNamesList(String projectId) {
    List<String> oldPublishers = getPublishers(oldDomainObjects.get(projectId));
    List<String> newPublishers = getPublishers(newDomainObjects.get(projectId));
    return ListUtils.diff(newPublishers, oldPublishers);
  }

  /**
   * Get user names of removed publishers.
   * 
   * @param projectId id of project where publishers removals should be checked
   * @return list of removed publisher user names
   */
  public List<String> getRemovedPublisherUserNamesList(String projectId) {
    List<String> oldPublishers = getPublishers(oldDomainObjects.get(projectId));
    List<String> newPublishers = getPublishers(newDomainObjects.get(projectId));
    return ListUtils.diff(oldPublishers, newPublishers);
  }

  /**
   * Get user names of added data providers.
   * 
   * @param projectId id of project where data provider additions should be checked
   * @return list of added data provider user names
   */
  public List<String> getAddedDataProviderUserNamesList(String projectId) {
    List<String> oldDataProviders = getDataProviders(oldDomainObjects.get(projectId));
    List<String> newDataProviders = getDataProviders(newDomainObjects.get(projectId));
    return ListUtils.diff(newDataProviders, oldDataProviders);
  }

  /**
   * Get user names of removed data providers.
   * 
   * @param projectId id of project where data provider removals should be checked
   * @return list of removed data provider user names
   */
  public List<String> getRemovedDataProviderUserNamesList(String projectId) {
    List<String> oldDataProviders = getDataProviders(oldDomainObjects.get(projectId));
    List<String> newDataProviders = getDataProviders(newDomainObjects.get(projectId));
    return ListUtils.diff(oldDataProviders, newDataProviders);
  }

  /**
   * Checks if the assignee group changed after the update.
   * 
   * @param projectId id of the project
   * @return {@code true} if assignee group changed, otherwise {@code false}
   */
  public boolean hasAssigneeGroupChanged(String projectId) {
    DataAcquisitionProject oldProject = oldDomainObjects.get(projectId);
    DataAcquisitionProject newProject = newDomainObjects.get(projectId);

    return oldProject != null && newProject != null
        && oldProject.getAssigneeGroup() != newProject.getAssigneeGroup();
  }

  /**
   * Get the assignee group of the updated project.
   * 
   * @param projectId id of the project
   * @return New assignee group
   */
  public AssigneeGroup getNewAssigneeGroup(String projectId) {
    DataAcquisitionProject dataAcquisitionProject = newDomainObjects.get(projectId);
    return dataAcquisitionProject != null ? dataAcquisitionProject.getAssigneeGroup() : null;
  }

  /**
   * Get the old {@link DataAcquisitionProject}.
   * 
   * @param projectId id of the project
   * @return {@link DataAcquisitionProject} or {@code null} if there was none
   */
  public DataAcquisitionProject getOldDataAcquisitionProject(String projectId) {
    return oldDomainObjects.get(projectId);
  }

  /**
   * Get new {@link DataAcquisitionProject}.
   * 
   * @param projectId id of the project
   * @return {@link DataAcquisitionProject} or {@code null} if there was none
   */
  public DataAcquisitionProject getNewDataAcquisitionProject(String projectId) {
    return newDomainObjects.get(projectId);
  }

  private List<String> getPublishers(DataAcquisitionProject project) {
    if (project != null) {
      return project.getConfiguration().getPublishers();
    } else {
      return Collections.emptyList();
    }
  }

  private List<String> getDataProviders(DataAcquisitionProject project) {
    if (project != null) {
      List<String> dataProviders = project.getConfiguration().getDataProviders();
      return dataProviders != null ? dataProviders : Collections.emptyList();
    } else {
      return Collections.emptyList();
    }
  }
}
