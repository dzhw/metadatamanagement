package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.AnswerOption;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;


/**
 * Controller for edit and create variables.
 * 
 * @author Amine Limouri
 */
@Controller
@RequestMapping(value = "/{language}/variables/create")
public class VariableModifyController {


  private VariableService variableService;
  private ControllerLinkBuilderFactory controllerLinkBuilderFactory;
  private Validator validator;

  /**
   * Autowire needed objects.
   * 
   * @param variableService his is a case with description
   * @param controllerLinkBuilderFactory his is a case with description
   * @param validator sdsdsds
   */
  @Autowired
  public VariableModifyController(VariableService variableService,
      ControllerLinkBuilderFactory controllerLinkBuilderFactory, Validator validator) {
    this.variableService = variableService;
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
    this.validator = validator;
  }

  /**
   * dfdfdfdfdf.
   * 
   * @return fgfgfgfg
   */
  @RequestMapping(method = RequestMethod.GET)
  public Callable<ModelAndView> get(
      VariableDocument variableDocument, BindingResult bindingResult) {
    return () -> {
      VariableModifyResource resource =
          new VariableModifyResource(VariableModifyController.class, controllerLinkBuilderFactory);

      validator.validate(createVariableDocument(), bindingResult);

      ModelAndView modelAndView = new ModelAndView("variables/modify");
      modelAndView.addObject("resource", resource);
      modelAndView.addObject("variableDocument", bindingResult.getModel().get("variableDocument"));

      return modelAndView;
    };
  }

  /**
   * adding sdsdsds.
   * 
   * @return sdsdsdsd
   */
  @RequestMapping(method = RequestMethod.POST, params = {"addOption"})
  public Callable<ModelAndView> addOption(
      @ModelAttribute VariableDocument variableDocument, BindingResult bindingResult) {
    return () -> {
      VariableModifyResource resource =
          new VariableModifyResource(VariableModifyController.class, controllerLinkBuilderFactory);
      variableDocument.getAnswerOptions().add(new AnswerOption());
      validator.validate(variableDocument, bindingResult);

      ModelAndView modelAndView = new ModelAndView("variables/modify");
      modelAndView.addObject("resource", resource);
      modelAndView.addObject("variableDocument", bindingResult.getModel().get("variableDocument"));

      return modelAndView;
    };
  }

  /**
   * add sdsdsds.
   * 
   * @return sdsdsdsd
   */
  @RequestMapping(method = RequestMethod.POST, params = {"removeOption"})
  public Callable<ModelAndView> removeOption(
      @ModelAttribute VariableDocument variableDocument, BindingResult bindingResult) {
    return () -> {
      VariableModifyResource resource =
          new VariableModifyResource(VariableModifyController.class, controllerLinkBuilderFactory);
      try {
        int lastIndex = variableDocument.getAnswerOptions().size();
        if (lastIndex > 1) {
          variableDocument.getAnswerOptions().remove(lastIndex - 1);
        }

      } catch (NullPointerException e) {
        e.printStackTrace();
      }
      validator.validate(variableDocument, bindingResult);
      ModelAndView modelAndView = new ModelAndView("variables/modify");
      modelAndView.addObject("resource", resource);
      modelAndView.addObject("variableDocument", bindingResult.getModel().get("variableDocument"));
      return modelAndView;
    };
  }

  /**
   * reset sdsdsds.
   * 
   * @return sdsdsdsd
   */
  @RequestMapping(method = RequestMethod.POST, params = {"reset"})
  public Callable<ModelAndView> reset() {
    return () -> {
      ModelAndView modelAndView = new ModelAndView("redirect:/{language}/variables/create");
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
  public Callable<ModelAndView> post(
      @Valid VariableDocument variableDocument, BindingResult bindingResult) {
    return () -> {
      ModelAndView modelAndView;
      VariableModifyResource resource =
          new VariableModifyResource(VariableModifyController.class, controllerLinkBuilderFactory);
      validator.validate(variableDocument, bindingResult);
      if (!bindingResult.hasErrors()) {
        variableService.save(variableDocument);
        modelAndView =
            new ModelAndView("redirect:/{language}/variables/" + variableDocument.getId());
        modelAndView.addObject("resource", resource);
      } else {
        modelAndView = new ModelAndView("variables/modify");
        modelAndView.addObject("resource", resource);
        modelAndView
            .addObject("variableDocument", bindingResult.getModel().get("variableDocument"));
      }
      return modelAndView;
    };
  }

  /**
   * dsdsdsdsd.
   * 
   * @return VariableDocument
   */
  private VariableDocument createVariableDocument() {
    VariableDocument document = new VariableDocument();
    VariableSurvey survey = new VariableSurvey();
    List<AnswerOption> answerOpt = new ArrayList<>();
    AnswerOption answer = new AnswerOption();
    answerOpt.add(answer);
    document.setAnswerOptions(answerOpt);
    document.setVariableSurvey(survey);

    return document;
  }

}
