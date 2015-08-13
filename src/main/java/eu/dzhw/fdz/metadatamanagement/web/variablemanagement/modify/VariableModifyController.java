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


/**
 * Controller for edit and create variables.
 * 
 * @author Amine Limouri
 */
@Controller
@RequestMapping(value = "/{language}/variables/create")
public class VariableModifyController {


  // @param variableService his is a case with description
  // private VariableService variableService;
  private ControllerLinkBuilderFactory controllerLinkBuilderFactory;
  private Validator validator;

  /**
   * Autowire needed objects.
   * 
   * @param controllerLinkBuilderFactory his is a case with description
   * @param validator sdsdsds
   */
  @Autowired
  public VariableModifyController(// VariableService variableService,
      ControllerLinkBuilderFactory controllerLinkBuilderFactory, Validator validator) {
    // this.variableService = variableService;
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
      @ModelAttribute VariableDocument variableDocument, BindingResult bindingResult) {
    return () -> {
      VariableModifyResource resource =
          new VariableModifyResource(VariableModifyController.class, controllerLinkBuilderFactory);


      validator.validate(createVariableDocument(), bindingResult);

      /*
       * Iterator<FieldError> it = bindingResult.getFieldErrors().iterator(); while (it.hasNext()) {
       * System.out.println(it.next().getDefaultMessage()); }
       */

      ModelAndView modelAndView = new ModelAndView("variables/modify");
      modelAndView.addObject("variableDocument", bindingResult.getModel().get("variableDocument"));
      modelAndView.addObject("resource", resource);
      return modelAndView;
    };
  }

  /**
   * adding sdsdsds.
   * 
   * @return sdsdsdsd
   */
  @RequestMapping(params = {"addOption"})
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
  @RequestMapping(params = {"removeOption"})
  public Callable<ModelAndView> removeOption(
      @ModelAttribute VariableDocument variableDocument, BindingResult bindingResult) {
    return () -> {
      VariableModifyResource resource =
          new VariableModifyResource(VariableModifyController.class, controllerLinkBuilderFactory);
      try {
        int lastIndex = variableDocument.getAnswerOptions().size();
        variableDocument.getAnswerOptions().remove(lastIndex - 1);
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

  /**
   * dsdsdsdsd.
   * 
   * @return VariableDocument
   */
  private VariableDocument createVariableDocument() {
    final VariableDocument document = new VariableDocument();
    final VariableSurvey survey = new VariableSurvey();
    final List<AnswerOption> answerOpt = new ArrayList<>();
    final AnswerOption answer = new AnswerOption();


    answerOpt.add(answer);
    document.setAnswerOptions(answerOpt);
    document.setVariableSurvey(survey);

    return document;
  }

}
