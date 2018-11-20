package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import eu.dzhw.fdz.metadatamanagement.common.service.util.ListUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Remember the previous version of a related data acquisition project per request
 */
@Component
@RequestScope
public class DataAcquisitionProjectChangesProvider {

  private Map<String, DataAcquisitionProject> oldProjects = new HashMap<>();
  private Map<String, DataAcquisitionProject> newProjects = new HashMap<>();

  void put(DataAcquisitionProject oldProject, DataAcquisitionProject newProject) {
    if (oldProject != null) {
      oldProjects.put(oldProject.getId(), oldProject);
    }

    if (newProject != null) {
      newProjects.put(newProject.getId(), newProject);
    }
  }

  /**
   * Get user names of added publishers.
   * @param projectId id of project where publishers additions should be checked
   * @return list of added publisher user names
   */
  public List<String> getAddedPublisherUserNamesList(String projectId) {
    List<String> oldPublishers = oldProjects.get(projectId).getConfiguration()
        .getPublishers();
    List<String> newPublishers = newProjects.get(projectId).getConfiguration()
        .getPublishers();

    return ListUtils.diff(newPublishers, oldPublishers);
  }

  /**
   * Get user names of removed publishers.
   * @param projectId id of project where publishers removals should be checked
   * @return list of removed publisher user names
   */
  public List<String> getRemovedPublisherUserNamesList(String projectId) {
    List<String> oldPublishers = oldProjects.get(projectId).getConfiguration()
        .getPublishers();
    List<String> newPublishers = newProjects.get(projectId).getConfiguration()
        .getPublishers();

    return ListUtils.diff(oldPublishers, newPublishers);
  }

  /**
   * Get user names of added data providers.
   * @param projectId id of project where data provider additions should be checked
   * @return list of added data provider user names
   */
  public List<String> getAddedDataProviderUserNamesList(String projectId) {
    List<String> oldDataProviders = oldProjects.get(projectId).getConfiguration()
        .getDataProviders();
    List<String> newDataProviders = newProjects.get(projectId).getConfiguration()
        .getDataProviders();

    if (oldDataProviders == null || oldDataProviders.isEmpty()) {
      return newDataProviders != null ? newDataProviders : Collections.emptyList();
    } else if (newDataProviders == null || newDataProviders.isEmpty()) {
      return Collections.emptyList();
    } else {
      return ListUtils.diff(newDataProviders, oldDataProviders);
    }
  }

  /**
   * Get user names of removed data providers.
   * @param projectId id of project where data provider removals should be checked
   * @return list of removed data provider user names
   */
  public List<String> getRemovedDataProviderUserNamesList(String projectId) {
    List<String> oldDataProviders = oldProjects.get(projectId).getConfiguration()
        .getDataProviders();
    List<String> newDataProviders = newProjects.get(projectId).getConfiguration()
        .getDataProviders();

    if (oldDataProviders == null || oldDataProviders.isEmpty()) {
      return newDataProviders != null ? newDataProviders : Collections.emptyList();
    } else if (newDataProviders == null || newDataProviders.isEmpty()) {
      return oldDataProviders;
    } else {
      return ListUtils.diff(oldDataProviders, newDataProviders);
    }
  }
}
