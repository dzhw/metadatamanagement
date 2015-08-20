package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
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
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.provider.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.provider.ScaleLevelProvider;
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
  private ScaleLevelProvider scaleLevelProvider;
  private DataTypesProvider dataTypesProvider;

  /**
   * Autowire needed objects.
   */
  @Autowired
  public VariableModifyController(VariableService variableService,
      ControllerLinkBuilderFactory controllerLinkBuilderFactory,
      VariableDocumentValidator validator, ScaleLevelProvider scaleLevelProvider,
      DataTypesProvider dataTypesProvider) {
    this.variableService = variableService;
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
    this.validator = validator;
    this.scaleLevelProvider = scaleLevelProvider;
    this.dataTypesProvider = dataTypesProvider;
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
      modelAndView.addObject("scaleLevelMap", scaleLevelProvider.getAllScaleLevel());
      modelAndView.addObject("dataTypesMap", dataTypesProvider.getAllDataTypes());
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
      VariableSurvey survey = new VariableSurvey();
      variableDocument.setVariableSurvey(survey);

      validator.validate(variableDocument, bindingResult);

      VariableModifyResource resource = new VariableModifyResource(VariableModifyController.class,
          VariableValidateController.class, controllerLinkBuilderFactory);

      ModelAndView modelAndView = new ModelAndView("variables/modify");
      modelAndView.addObject("resource", resource);
      modelAndView.addObject("focusElementId", "removeSurveyButton");
      modelAndView.addObject("scaleLevelMap", scaleLevelProvider.getAllScaleLevel());
      modelAndView.addObject("dataTypesMap", dataTypesProvider.getAllDataTypes());
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
      modelAndView.addObject("focusElementId", "addSurveyButton");
      modelAndView.addObject("scaleLevelMap", scaleLevelProvider.getAllScaleLevel());
      modelAndView.addObject("dataTypesMap", dataTypesProvider.getAllDataTypes());
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
      if (variableDocument.getAnswerOptions() == null) {
        List<AnswerOption> answerOpt = new ArrayList<>();
        variableDocument.setAnswerOptions(answerOpt);
      }
      variableDocument.getAnswerOptions().add(new AnswerOption());
      validator.validate(variableDocument, bindingResult);

      VariableModifyResource resource = new VariableModifyResource(VariableModifyController.class,
          VariableValidateController.class, controllerLinkBuilderFactory);

      ModelAndView modelAndView = new ModelAndView("variables/modify");
      modelAndView.addObject("focusElementId", "addAnswerOptionButton");
      modelAndView.addObject("resource", resource);
      modelAndView.addObject("scaleLevelMap", scaleLevelProvider.getAllScaleLevel());
      modelAndView.addObject("dataTypesMap", dataTypesProvider.getAllDataTypes());
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
      @RequestParam("removeAnswerOption") int indexAnswerOption, HttpServletRequest request,
      VariableDocument variableDocument, BindingResult bindingResult) {
    return () -> {
      variableDocument.getAnswerOptions().remove(indexAnswerOption);
      validator.validate(variableDocument, bindingResult);


      VariableModifyResource resource = new VariableModifyResource(VariableModifyController.class,
          VariableValidateController.class, controllerLinkBuilderFactory);

      ModelAndView modelAndView = new ModelAndView("variables/modify");
      modelAndView.addObject("resource", resource);
      modelAndView.addObject("focusElementId", "addAnswerOptionButton");
      modelAndView.addObject("scaleLevelMap", scaleLevelProvider.getAllScaleLevel());
      modelAndView.addObject("dataTypesMap", dataTypesProvider.getAllDataTypes());
      modelAndView.addObject("variableDocument", variableDocument);
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
        modelAndView.addObject("scaleLevelMap", scaleLevelProvider.getAllScaleLevel());
        modelAndView.addObject("dataTypesMap", dataTypesProvider.getAllDataTypes());
        modelAndView.addObject("variableDocument", variableDocument);
      }
      return modelAndView;
    };
  }
}
