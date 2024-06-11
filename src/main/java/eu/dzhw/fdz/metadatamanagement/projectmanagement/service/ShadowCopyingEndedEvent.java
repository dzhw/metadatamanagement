package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import org.springframework.context.ApplicationEvent;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowCopyQueueItem.Action;
import lombok.Getter;

/**
 * Event emitted if a project release is detected. Contains the release version and projectId of
 * {@link DataAcquisitionProject} as a reference.
 */
@Getter
public class ShadowCopyingEndedEvent extends ApplicationEvent {

  private static final long serialVersionUID = 549634157200620526L;

  private String dataAcquisitionProjectId;

  private String previousReleaseVersion;

  private Release release;

  private boolean isRerelease;

  private boolean isReleaseAfterPreRelease;

  private Action action;

  /**
   * Create a new event instance.
   *
   * @param source Event emitter reference
   * @param dataAcquisitionProjectId master id of the released project
   * @param release the version which has been released
   * @param previousReleaseVersion the previous version or null
   * @param isRerelease true if the project has been released with this version before
   */
  public ShadowCopyingEndedEvent(Object source, String dataAcquisitionProjectId,
      Release release, String previousReleaseVersion, boolean isRerelease,
      Action action, boolean isReleaseAfterPreRelease) {
    super(source);
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
    this.previousReleaseVersion = previousReleaseVersion;
    this.release = release;
    this.isRerelease = isRerelease;
    this.isReleaseAfterPreRelease = isReleaseAfterPreRelease;
    this.action = action;
  }
}
