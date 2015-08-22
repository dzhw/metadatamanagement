package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import java.util.Optional;
import java.util.concurrent.Callable;

import javax.validation.Valid;

import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.AnswerOption;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.VariableDocumentValidator;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;

/**
 * Base Controller for {@link VariableCreateController} and {@link VariableEditController}.
 * 
 * @author René Reitmann
 */
public abstract class AbstractVariableModifyController {

  protected VariableService variableService;
  protected ControllerLinkBuilderFactory controllerLinkBuilderFactory;
  protected VariableDocumentValidator validator;
  protected ScaleLevelProvider scaleLevelProvider;
  protected DataTypesProvider dataTypesProvider;

  /**
   * Create the controller.
   * 
   * @param variableService the service managing the variable state
   * @param controllerLinkBuilderFactory a factory for building links to resources
   * @param validator the validator for variables
   * @param scaleLevelProvider a provider returning valid scalelevels
   * @param dataTypesProvider a provider returning valid datatypes
   */
  protected AbstractVariableModifyController(VariableService variableService,
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

  protected ModelAndView createModelAndView(VariableDocument variableDocument,
      Optional<String> focusElementId) {
    ModelAndView modelAndView = new ModelAndView("variables/modify");
    modelAndView.addObject("resource", createResource(variableDocument.getId()));
    modelAndView.addObject("scaleLevelMap", scaleLevelProvider.getAllScaleLevel());
    modelAndView.addObject("dataTypesMap", dataTypesProvider.getAllDataTypes());
    modelAndView.addObject("variableDocument", variableDocument);
    if (focusElementId.isPresent()) {
      modelAndView.addObject("focusElementId", focusElementId.get());
    }
    return modelAndView;
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

      return createModelAndView(variableDocument, Optional.of("removeSurveyButton"));
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
      variableDocument.setVariableSurvey(null);

      validator.validate(variableDocument, bindingResult);

      return createModelAndView(variableDocument, Optional.of("addSurveyButton"));
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

      return createModelAndView(variableDocument, Optional.of("addAnswerOptionButton"));
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
      variableDocument.getAnswerOptions().remove(indexAnswerOption);
      validator.validate(variableDocument, bindingResult);

      return createModelAndView(variableDocument, Optional.of("addAnswerOptionButton"));
    };
  }

  /**
   * save a valid variable document.
   * 
   * @return variableDetails.html or modify.html
   */
  @RequestMapping(method = RequestMethod.POST)
  public Callable<ModelAndView> save(@Valid VariableDocument variableDocument,
      BindingResult bindingResult) {
    return () -> {
      if (!bindingResult.hasErrors()) {
        variableService.save(variableDocument);
        // TODO remove hardcoded url
        return new ModelAndView("redirect:/{language}/variables/" + variableDocument.getId());
      } else {

        return createModelAndView(variableDocument, Optional.empty());
      }
    };
  }

  protected abstract AbstractVariableModifyResource createResource(String variableId);
}
