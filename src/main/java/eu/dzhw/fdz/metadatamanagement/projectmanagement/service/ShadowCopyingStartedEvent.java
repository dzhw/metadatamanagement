package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import org.springframework.context.ApplicationEvent;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import lombok.Getter;

/**
 * Event emitted if a project release is detected. Contains the release version and projectId of
 * {@link DataAcquisitionProject} as a reference.
 */
@Getter
public class ShadowCopyingStartedEvent extends ApplicationEvent {

  private static final long serialVersionUID = 1549537610524235042L;

  private String dataAcquisitionProjectId;

  private String previousReleaseVersion;

  private Release release;

  /**
   * Create a new event instance.
   * 
   * @param source Event emitter reference
   * @param dataAcquisitionProjectId id of the released project
   * @param release the version which has been released
   * @param previousReleaseVersion the previous version or null
   */
  public ShadowCopyingStartedEvent(Object source, String dataAcquisitionProjectId,
      Release release, String previousReleaseVersion) {
    super(source);
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
    this.previousReleaseVersion = previousReleaseVersion;
    this.release = release;
  }
}
