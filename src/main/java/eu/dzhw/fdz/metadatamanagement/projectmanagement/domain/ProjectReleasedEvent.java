package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Event emitted if a project release is detected. Contains the release version
 * and projectId of {@link DataAcquisitionProject} as a reference.
 */
@Getter
public class ProjectReleasedEvent extends ApplicationEvent {

  private static final long serialVersionUID = 1549537610524235042L;

  private DataAcquisitionProject dataAcquisitionProject;

  /**
   * Create a new event instance.
   * @param source Event emitter reference
   * @param dataAcquisitionProject Released project
   */
  public ProjectReleasedEvent(Object source, DataAcquisitionProject dataAcquisitionProject) {
    super(source);
    this.dataAcquisitionProject = dataAcquisitionProject;
  }
}
