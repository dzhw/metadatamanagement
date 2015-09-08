package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.web.common.NavigatablePageResource;

/**
 * Resource holding the links for the details page.
 * 
 * 
 * @author Amine limouri
 *
 */
public class VariableDetailsResource extends NavigatablePageResource<VariableDetailsController> {
  private VariableResource variableResource;

  /**
   * Create the details resource for the variable details template with navigation and edit links.
   * 
   * @param controllerLinkBuilderFactory the {@link ControllerLinkBuilderFactory}
   * @param variableResource the document as resource
   */
  public VariableDetailsResource(ControllerLinkBuilderFactory controllerLinkBuilderFactory,
      VariableResource variableResource) {
    super();
    this.variableResource = variableResource;
    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      this.add(controllerLinkBuilderFactory.linkTo(
          methodOn(VariableDetailsController.class, supportedLocale).get(
              variableResource.getVariableDocument().getId())).withRel(
          supportedLocale.getLanguage()));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((variableResource == null) ? 0 : variableResource.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    VariableDetailsResource other = (VariableDetailsResource) obj;
    if (variableResource == null) {
      if (other.variableResource != null) {
        return false;
      }
    } else if (!variableResource.equals(other.variableResource)) {
      return false;
    }
    return true;
  }

  public VariableResource getVariableResource() {
    return variableResource;
  }

  public void setVariableResource(VariableResource variableResource) {
    this.variableResource = variableResource;
  }

}
