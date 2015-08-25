package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import java.util.Optional;

import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import eu.dzhw.fdz.metadatamanagement.web.common.NavigatablePageResource;

/**
 * Base resource for the two variable modify resources.
 * 
 * @author René Reitmann
 */
public abstract class AbstractVariableModifyResource
    extends NavigatablePageResource<VariableCreateController> {

  /**
   * Create the validate link which is used in both resources ({@link VariableEditResource} and
   * {@link VariableCreateResource}).
   * 
   * @param factory The {@link ControllerLinkBuilderFactory}
   */
  public AbstractVariableModifyResource(ControllerLinkBuilderFactory factory,
      Optional<String> variableId) {
    super();
    this.addValidateLink(factory, variableId);
  }

  /**
   * This method at the validation link to the resource.
   * 
   * @param factory A factory which links the validate link to the resource.
   * @param variableId The optional validateId, which is a part of the link at edit variable
   */
  protected abstract void addValidateLink(ControllerLinkBuilderFactory factory,
      Optional<String> variableId);



  /**
   * Indicate the mode of the page (create or edit).
   * 
   * @return true if the template should be viewed in create mode
   */
  public abstract boolean isCreateMode();

  /**
   * Get the link to which the modified variable must be posted for saving.
   * 
   * @return the link to which the modified variable must be posted for saving
   */
  public String getSaveLink() {
    return this.getLink("save").getHref();
  }

  /**
   * Get the link to which the modified variable must be posted for validation.
   * 
   * @return the link to which the modified variable must be posted for validation
   */
  public String getValidateLink() {
    return this.getLink("validate").getHref();
  }

}
