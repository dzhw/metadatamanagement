package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.common.exceptions.DocumentNotFoundException;

/**
 * A Controller which returns a details page for a variable or error a page.
 * 
 */
@Controller
@RequestMapping(path = "/{language:de|en}/variables")
public class VariableDetailsController {

  private VariableService variableService;
  protected ControllerLinkBuilderFactory controllerLinkBuilderFactory;

  /**
   * Create the controller.
   * 
   * @param variableService the service managing the variable state
   * @param controllerLinkBuilderFactory a factory for building links to resources
   */
  @Autowired
  public VariableDetailsController(VariableService variableService,
      ControllerLinkBuilderFactory controllerLinkBuilderFactory) {
    this.variableService = variableService;
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
  }

  /**
   * return the details or throw exception.
   * 
   * @return details.html
   */
  @RequestMapping(path = "/{variableId}", method = RequestMethod.GET)
  public Callable<ModelAndView> get(@PathVariable("variableId") String variableId) {
    return () -> {
      VariableDocument variableDocument = variableService.get(variableId);
      if (variableDocument == null) {
        throw new DocumentNotFoundException(variableId, LocaleContextHolder.getLocale(),
            VariableDocument.class);
      } else {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("variables/details");
        modelAndView.addObject("variableDocument", variableDocument);
        modelAndView.addObject("resource", new VariableDetailsResource(
            controllerLinkBuilderFactory, variableDocument.getId()));
        return modelAndView;
      }
    };
  }
}
