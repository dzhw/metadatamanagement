package eu.dzhw.fdz.metadatamanagement.web;

import java.util.Locale;
import java.util.concurrent.Callable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.dzhw.fdz.metadatamanagement.config.i18n.PathLocaleResolver;

/**
 * Simple Controller which returns a welcome page for all requests which contain a supported
 * language in the path.
 * 
 * @author René Reitmann
 */
@Controller
public class WelcomeController {

  /**
   * Show welcome page.
   * 
   * @return welcome.html
   */
  @RequestMapping(path = "/{language}", method = RequestMethod.GET)
  public final Callable<String> showWelcomePage() {
    return () -> {
      return "welcome";
    };
  }

  /**
   * Redirect the request from the context root to a supported locale.
   * 
   * @param locale The locale as parsed from the {@link PathLocaleResolver}
   * @return A redirect to the localized welcome page.
   */
  @RequestMapping(path = "/", method = RequestMethod.GET)
  public final Callable<String> redirectToLocalizedPage(Locale locale) {
    return () -> {
      return "redirect:/" + locale.getLanguage();
    };
  }
}
