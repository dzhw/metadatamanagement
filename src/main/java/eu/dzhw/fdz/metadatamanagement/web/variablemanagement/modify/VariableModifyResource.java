package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.web.common.NavigatablePageResource;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.validate.VariableValidateController;

/**
 * The Resource for the variable modify template.
 */
public class VariableModifyResource extends NavigatablePageResource<VariableModifyController> {

  /**
   * Create the resource for the variable modify template with self link and validate link as well
   * as the navigation links.
   * 
   * @param modifyController the {@link VariableModifyController}
   * @param validateController the {@link VariableValidateController}
   * @param factory the {@link ControllerLinkBuilderFactory}
   */
  public VariableModifyResource(Class<VariableModifyController> modifyController,
      Class<VariableValidateController> validateController, ControllerLinkBuilderFactory factory) {
    super();
    this.add(factory.linkTo(
        methodOn(modifyController, LocaleContextHolder.getLocale().getLanguage()).get(null, null))
        .withRel(Link.REL_SELF));
    this.add(
        factory.linkTo(methodOn(validateController, LocaleContextHolder.getLocale().getLanguage())
            .validate(null, null, null)).withRel("validate"));
    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      this.add(factory.linkTo(methodOn(modifyController, supportedLocale).get(null, null))
          .withRel(supportedLocale.getLanguage()));
    }
  }

}
