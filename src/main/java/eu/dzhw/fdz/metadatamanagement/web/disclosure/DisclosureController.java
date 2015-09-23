package eu.dzhw.fdz.metadatamanagement.web.disclosure;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Simple Controller which returns a disclosure page for all requests which contain a supported
 * language in the path.
 * 
 * @author Amine Limouri
 */
@Controller
public class DisclosureController {

  private final ControllerLinkBuilderFactory controllerLinkBuilderFactory;

  @Autowired
  public DisclosureController(ControllerLinkBuilderFactory controllerLinkBuilderFactory) {
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
  }

  /**
   * Shows disclosure page.
   * 
   * @return disclosure.html
   */
  @RequestMapping(path = "/{language:de|en}/disclosure", method = RequestMethod.GET)
  public Callable<ModelAndView> get() {
    return () -> {
      DisclosureControllerResource resource =
          new DisclosureControllerResource(DisclosureController.class, 
              controllerLinkBuilderFactory);
      ModelAndView result = new ModelAndView("common/disclosure");
      result.addObject("resource", resource);
      return result;
    };
  }
}
