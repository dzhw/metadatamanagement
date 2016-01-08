package eu.dzhw.fdz.metadatamanagement.service.event;

import org.springframework.context.ApplicationEvent;

import eu.dzhw.fdz.metadatamanagement.service.FdzProjectService;

/**
 * This Event will be fired by the {@link FdzProjectService}, if a fdz project has been deleted.
 * 
 * @author Daniel Katzberg
 */
public class FdzProjectDeletedEvent extends ApplicationEvent {

  private static final long serialVersionUID = 1L;

  private String fdzProjectName;
  
  /**
   * Creates the event with a fdz project name.
   * 
   * @param fdzProjectName The name of a fdz project name.
   */
  public FdzProjectDeletedEvent(String fdzProjectName) {
    super(fdzProjectName);
    this.fdzProjectName = fdzProjectName;
  }

  /* GETTER / SETTER */
  public String getFdzProjectName() {
    return fdzProjectName;
  }
}
