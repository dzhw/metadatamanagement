package eu.dzhw.fdz.metadatamanagement.web.welcome;

import java.util.Locale;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.config.i18n.PathLocaleResolver;

/**
 * Simple Controller which returns a welcome page for all requests which contain a supported
 * language in the path.
 * 
 * @author René Reitmann
 */
@Controller
public class WelcomeController {

  private final ControllerLinkBuilderFactory controllerLinkBuilderFactory;

  @Autowired
  public WelcomeController(ControllerLinkBuilderFactory controllerLinkBuilderFactory) {
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
  }

  /**
   * Show welcome page.
   * 
   * @return welcome.html
   */
  @RequestMapping(path = "/{language:de|en}", method = RequestMethod.GET)
  public Callable<ModelAndView> get() {
    return () -> {
      WelcomePageResource resource =
          new WelcomePageResource(WelcomeController.class, controllerLinkBuilderFactory);

      ModelAndView result = new ModelAndView("welcome");
      result.addObject("resource", resource);
      return result;
    };
  }

  /**
   * Redirect the request from the context root to a supported locale.
   * 
   * @param locale The locale as parsed from the {@link PathLocaleResolver}
   * @return A redirect to the localized welcome page.
   */
  @RequestMapping(path = "/", method = RequestMethod.GET)
  public Callable<String> redirectToLocalizedPage(Locale locale) {
    return () -> {
      return "redirect:/" + locale.getLanguage();
    };
  }
}
