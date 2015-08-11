package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import java.util.concurrent.Callable;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;


/**
 * Controller for edit and create variables.
 * 
 * @author Amine Limouri
 */
@Controller
@RequestMapping(value = "/{language}/variables/create")
public class VariableModifyController {


  // private VariableService variableService;
  private ControllerLinkBuilderFactory controllerLinkBuilderFactory;

  /**
   * Autowire needed objects.
   * 
   * @param variableService his is a case with description
   * @param controllerLinkBuilderFactory his is a case with description
   */
  @Autowired
  public VariableModifyController(// VariableService variableService,
      ControllerLinkBuilderFactory controllerLinkBuilderFactory) {
    // this.variableService = variableService;
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
  }

  /**
   * dfdfdfdfdf.
   * 
   * @return fgfgfgfg
   */
  @RequestMapping(method = RequestMethod.GET)
  public Callable<ModelAndView> get() {
    return () -> {
      VariableModifyResource resource =
          new VariableModifyResource(VariableModifyController.class, controllerLinkBuilderFactory);
      ModelAndView modelAndView = new ModelAndView("variables/modify");
      modelAndView.addObject("resource", resource);
      return modelAndView;
    };
  }

  /**
   * dfdfdfdfdf.
   * 
   * @param variableDocument fgfgfgfg
   * @param bindingResult fgfggfgf
   * @return fgfgfgfg
   */
  @RequestMapping(method = RequestMethod.POST)
  public Callable<String> post(
      @Valid VariableDocument variableDocument, BindingResult bindingResult) {
    return () -> {
      return "redirect:/{language}/variables/" + "FdZ_ID07";
    };
  }

}
