package eu.dzhw.fdz.metadatamanagement.service.event;

import org.springframework.context.ApplicationEvent;

import eu.dzhw.fdz.metadatamanagement.service.FdzProjectService;

/**
 * This Event will be fired by the FdzProjectService, if a fdz project will be delete.
 * 
 * @see FdzProjectService
 * 
 * @author Daniel Katzberg
 */
public class FdzProjectDeleteEvent extends ApplicationEvent {

  private static final long serialVersionUID = 1L;

  private String fdzProjectName;
  
  /**
   * Creates a FdZ Project Delete Event with a fdz project name.
   * 
   * @param fdzProjectName The name of a fdz project name.
   */
  public FdzProjectDeleteEvent(String fdzProjectName) {
    super(fdzProjectName);
    this.fdzProjectName = fdzProjectName;
  }

  /* GETTER / SETTER */
  public String getFdzProjectName() {
    return fdzProjectName;
  }
}
