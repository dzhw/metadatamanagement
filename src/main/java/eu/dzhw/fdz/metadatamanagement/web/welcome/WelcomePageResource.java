package eu.dzhw.fdz.metadatamanagement.web.welcome;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.web.common.NavigatablePageResource;

/**
 * Resource which holds the links for the welcome page.
 * 
 * @author René Reitmann
 */
public class WelcomePageResource extends NavigatablePageResource<WelcomeController> {

  /**
   * Create the navigation links and the i18n links.
   * 
   * @param pageController the controller for the welcome page
   * @param factory the controller link factory which can resolve query params
   */
  public WelcomePageResource(Class<WelcomeController> pageController,
      ControllerLinkBuilderFactory factory) {
    super();
    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      this.add(factory.linkTo(methodOn(pageController, supportedLocale).get())
          .withRel(supportedLocale.getLanguage()));
    }
  }

}
