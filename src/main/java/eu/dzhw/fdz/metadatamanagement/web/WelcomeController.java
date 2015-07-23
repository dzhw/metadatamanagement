package eu.dzhw.fdz.metadatamanagement.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Simple Controller which returns a welcome page.
 * 
 * @author René Reitmann
 */
@Controller
@RequestMapping("/")
public class WelcomeController {

  /**
   * Show welcome page.
   * 
   * @return welcome.html
   */
  @RequestMapping(method = RequestMethod.GET)
  public final DeferredResult<String> showWelcomePage() {
    DeferredResult<String> result = new DeferredResult<String>();
    result.setResult("welcome");
    return result;
  }
}
