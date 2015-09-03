package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.web.common.NavigatablePageResource;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify.VariableEditController;

/**
 * Resource holding the links for the details page.
 * 
 * 
 * @author Amine limouri
 *
 */
public class VariableDetailsResource extends NavigatablePageResource<VariableDetailsController> {

  /**
   * Create the details resource for the variable details template with navigation and edit links.
   * 
   * @param controllerLinkBuilderFactory the {@link ControllerLinkBuilderFactory}
   * @param variableId the id of the document
   */
  public VariableDetailsResource(ControllerLinkBuilderFactory controllerLinkBuilderFactory,
          String variableId) {
    super();
    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      this.add(controllerLinkBuilderFactory.linkTo(
              methodOn(VariableDetailsController.class, supportedLocale).get(variableId)).withRel(
              supportedLocale.getLanguage()));
    }
    this.add(controllerLinkBuilderFactory.linkTo(
            methodOn(VariableEditController.class, LocaleContextHolder.getLocale().getLanguage())
                    .edit(variableId)).withRel("edit"));
  }

}
