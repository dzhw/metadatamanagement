package eu.dzhw.fdz.metadatamanagement.web.impressum;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.web.common.NavigatablePageResource;

/**
 * Resource which holds the links for the disclosure page.
 * 
 * @author Amine Limouri
 */
public class DisclosureControllerResource extends NavigatablePageResource<DisclosureController> {

  /**
   * Create the navigation links and the i18n links.
   * 
   * @param pageController the controller for the Legal disclosure page
   * @param factory the controller link factory which can resolve query params
   */
  public DisclosureControllerResource(Class<DisclosureController> pageController,
      ControllerLinkBuilderFactory factory) {
    super();
    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      this.add(factory.linkTo(methodOn(pageController, supportedLocale).get()).withRel(
          supportedLocale.getLanguage()));
    }
  }

}
