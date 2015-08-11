package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.web.common.NavigatablePageResource;

/**
 * sdssdsdsd.
 * 
 * @author limouri
 *
 */
public class VariableModifyResource extends NavigatablePageResource<VariableModifyController> {

  /**
   * dfdfdfdf.
   * 
   * @param modifyController dfd
   * @param factory dfdf
   */
  public VariableModifyResource(Class<VariableModifyController> modifyController,
      ControllerLinkBuilderFactory factory) {
    super();
    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      this.add(factory.linkTo(methodOn(modifyController, supportedLocale).get()).withRel(
          supportedLocale.getLanguage()));
    }
  }

}
