package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import eu.dzhw.fdz.metadatamanagement.web.common.NavigatablePageResource;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.validate.VariableValidateController;

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
  public AbstractVariableModifyResource(ControllerLinkBuilderFactory factory) {
    super();
    this.add(factory
        .linkTo(methodOn(VariableValidateController.class,
            LocaleContextHolder.getLocale().getLanguage()).validate(null, null, null))
        .withRel("validate"));
  }

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
