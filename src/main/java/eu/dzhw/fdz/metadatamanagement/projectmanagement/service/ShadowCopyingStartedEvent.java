package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import org.springframework.context.ApplicationEvent;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowCopyQueueItem.Action;
import lombok.Getter;

/**
 * Event emitted if a project release is detected or a shadow need to be hidden/unhidden. Contains
 * the release version and projectId of {@link DataAcquisitionProject} as a reference.
 */
@Getter
public class ShadowCopyingStartedEvent extends ApplicationEvent {

  private static final long serialVersionUID = 1549537610524235042L;

  private String dataAcquisitionProjectId;

  private String previousReleaseVersion;

  private Release release;

  private Action action;

  /**
   * Create a new event instance.
   * 
   * @param source Event emitter reference
   * @param dataAcquisitionProjectId id of the released project
   * @param release the version which has been released
   * @param previousReleaseVersion the previous version or null
   * @param action The action to be executed.
   */
  public ShadowCopyingStartedEvent(Object source, String dataAcquisitionProjectId, Release release,
      String previousReleaseVersion, Action action) {
    super(source);
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
    this.previousReleaseVersion = previousReleaseVersion;
    this.release = release;
    this.action = action;
  }
}
