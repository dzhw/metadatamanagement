package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import java.util.concurrent.Callable;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.AnswerOption;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.VariableDocumentValidator;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.validate.VariableValidateController;


/**
 * Controller for edit and create variables.
 * 
 * @author Amine Limouri
 */
@Controller
@RequestMapping(value = "/{language:de|en}/variables/create")
public class VariableModifyController {
  private VariableService variableService;
  private ControllerLinkBuilderFactory controllerLinkBuilderFactory;
  private VariableDocumentValidator validator;

  /**
   * Autowire needed objects.
   */
  @Autowired
  public VariableModifyController(VariableService variableService,
      ControllerLinkBuilderFactory controllerLinkBuilderFactory,
      VariableDocumentValidator validator) {
    this.variableService = variableService;
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
    this.validator = validator;
  }

  @InitBinder("variableDocument")
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(validator);
  }

  /**
   * Show edit and create variable page.
   * 
   * @return modify.html
   */
  @RequestMapping(method = RequestMethod.GET)
  public Callable<ModelAndView> get(@Valid VariableDocument variableDocument,
      BindingResult bindingResult) {
    return () -> {
      VariableModifyResource resource = new VariableModifyResource(VariableModifyController.class,
          VariableValidateController.class, controllerLinkBuilderFactory);

      ModelAndView modelAndView = new ModelAndView("variables/modify");
      modelAndView.addObject("resource", resource);
      modelAndView.addObject("variableDocument", variableDocument);

      return modelAndView;
    };
  }


  /**
   * add a survey to the variable.
   * 
   * @return modify.html
   */
  @RequestMapping(method = RequestMethod.POST, params = {"addSurvey"})
  public Callable<ModelAndView> addSurvey(VariableDocument variableDocument,
      BindingResult bindingResult) {
    return () -> {
      variableDocument.setVariableSurvey(new VariableSurvey());

      validator.validate(variableDocument, bindingResult);

      VariableModifyResource resource = new VariableModifyResource(VariableModifyController.class,
          VariableValidateController.class, controllerLinkBuilderFactory);
      ModelAndView modelAndView = new ModelAndView("variables/modify");
      modelAndView.addObject("resource", resource);
      modelAndView.addObject("variableDocument", variableDocument);

      return modelAndView;
    };
  }

  /**
   * remove a survey from the variable.
   * 
   * @return modify.html
   */
  @RequestMapping(method = RequestMethod.POST, params = {"removeSurvey"})
  public Callable<ModelAndView> removeSurvey(VariableDocument variableDocument,
      BindingResult bindingResult) {
    return () -> {
      VariableModifyResource resource = new VariableModifyResource(VariableModifyController.class,
          VariableValidateController.class, controllerLinkBuilderFactory);
      variableDocument.setVariableSurvey(null);
      validator.validate(variableDocument, bindingResult);
      ModelAndView modelAndView = new ModelAndView("variables/modify");
      modelAndView.addObject("resource", resource);
      modelAndView.addObject("variableDocument", variableDocument);
      return modelAndView;
    };
  }

  /**
   * add an answer option to the variable.
   * 
   * @return modify.html
   */
  @RequestMapping(method = RequestMethod.POST, params = {"addAnswerOption"})
  public Callable<ModelAndView> addAnswerOption(VariableDocument variableDocument,
      BindingResult bindingResult) {
    return () -> {
      variableDocument.addAnswerOption(new AnswerOption());
      validator.validate(variableDocument, bindingResult);
      VariableModifyResource resource = new VariableModifyResource(VariableModifyController.class,
          VariableValidateController.class, controllerLinkBuilderFactory);
      ModelAndView modelAndView = new ModelAndView("variables/modify");
      modelAndView.addObject("resource", resource);
      modelAndView.addObject("variableDocument", variableDocument);

      return modelAndView;
    };
  }

  /**
   * remove an answer option from the variable.
   * 
   * @return modify.html
   */
  @RequestMapping(method = RequestMethod.POST, params = {"removeAnswerOption"})
  public Callable<ModelAndView> removeAnswerOption(
      @RequestParam("removeAnswerOption") int indexAnswerOption, VariableDocument variableDocument,
      BindingResult bindingResult) {
    return () -> {
      variableDocument.removeAnswerOption(indexAnswerOption);
      validator.validate(variableDocument, bindingResult);

      VariableModifyResource resource = new VariableModifyResource(VariableModifyController.class,
          VariableValidateController.class, controllerLinkBuilderFactory);
      ModelAndView modelAndView = new ModelAndView("variables/modify");
      modelAndView.addObject("resource", resource);
      modelAndView.addObject("variableDocument", variableDocument);
      return modelAndView;
    };
  }

  /**
   * reload the create and edit dialog.
   * 
   * @return modify.html
   */
  @RequestMapping(method = RequestMethod.POST, params = {"reset"})
  public Callable<ModelAndView> reset() {
    return () -> {
      ModelAndView modelAndView = new ModelAndView("redirect:/{language}/variables/create");
      return modelAndView;
    };
  }

  /**
   * save a valid variable document.
   * 
   * @return variableDetails.html or modify.html
   */
  @RequestMapping(method = RequestMethod.POST)
  public Callable<ModelAndView> postVariableDocument(@Valid VariableDocument variableDocument,
      BindingResult bindingResult) {
    return () -> {
      ModelAndView modelAndView;
      if (!bindingResult.hasErrors()) {
        variableService.save(variableDocument);
        modelAndView =
            new ModelAndView("redirect:/{language}/variables/" + variableDocument.getId());
      } else {
        modelAndView = new ModelAndView("variables/modify");
        VariableModifyResource resource = new VariableModifyResource(VariableModifyController.class,
            VariableValidateController.class, controllerLinkBuilderFactory);
        modelAndView.addObject("resource", resource);
        modelAndView.addObject("variableDocument", variableDocument);
      }
      return modelAndView;
    };
  }
}
