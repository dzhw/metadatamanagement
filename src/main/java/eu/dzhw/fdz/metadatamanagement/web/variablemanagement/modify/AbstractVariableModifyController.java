package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.AnswerOption;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators.VariableDocumentValidator;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.ValidationResultDto;

/**
 * Base Controller for {@link VariableCreateController} and {@link VariableEditController}.
 * 
 * @author René Reitmann
 */
public abstract class AbstractVariableModifyController {

  public static final String VARIABLE_PATH = "variables/";
  public static final String LANGUAGES_PATH = "{language}/";

  protected VariableService variableService;
  protected ControllerLinkBuilderFactory controllerLinkBuilderFactory;
  protected VariableDocumentValidator validator;
  protected ScaleLevelProvider scaleLevelProvider;
  protected DataTypesProvider dataTypesProvider;

  @Autowired
  private MessageSource messageSource;

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
      Optional<String> focusElementId, Integer windowYPosition) {
    ModelAndView modelAndView = new ModelAndView("variables/modify");
    modelAndView.addObject("resource", createResource(variableDocument.getId()));
    modelAndView.addObject("scaleLevelMap", scaleLevelProvider.getAllScaleLevel());
    modelAndView.addObject("dataTypesMap", dataTypesProvider.getAllDataTypes());
    modelAndView.addObject("variableDocument", variableDocument);
    if (focusElementId.isPresent()) {
      modelAndView.addObject("focusElementId", focusElementId.get());
    }
    modelAndView.addObject("windowYPosition", windowYPosition);
    return modelAndView;
  }

  /**
   * add a survey to the variable.
   * 
   * @return modify.html
   */
  @RequestMapping(method = RequestMethod.POST, params = {"addSurvey"})
  public Callable<ModelAndView> addSurvey(VariableDocument variableDocument,
      BindingResult bindingResult, Integer windowYPosition) {
    return () -> {
      VariableSurvey survey = new VariableSurvey();
      variableDocument.setVariableSurvey(survey);

      validator.validate(variableDocument, bindingResult);

      return createModelAndView(variableDocument, Optional.of("removeSurveyButton"),
          windowYPosition);
    };
  }

  /**
   * remove a survey from the variable.
   * 
   * @return modify.html
   */
  @RequestMapping(method = RequestMethod.POST, params = {"removeSurvey"})
  public Callable<ModelAndView> removeSurvey(VariableDocument variableDocument,
      BindingResult bindingResult, Integer windowYPosition) {
    return () -> {
      variableDocument.setVariableSurvey(null);

      validator.validate(variableDocument, bindingResult);

      return createModelAndView(variableDocument, Optional.of("addSurveyButton"), windowYPosition);
    };
  }

  /**
   * add an answer option to the variable.
   * 
   * @return modify.html
   */
  @RequestMapping(method = RequestMethod.POST, params = {"addAnswerOption"})
  public Callable<ModelAndView> addAnswerOption(VariableDocument variableDocument,
      BindingResult bindingResult, Integer windowYPosition) {
    return () -> {
      variableDocument.addAnswerOption(new AnswerOption());

      validator.validate(variableDocument, bindingResult);

      return createModelAndView(variableDocument, Optional.of("addAnswerOptionButton"),
          windowYPosition);
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
      BindingResult bindingResult, Integer windowYPosition) {
    return () -> {
      variableDocument.getAnswerOptions().remove(indexAnswerOption);
      validator.validate(variableDocument, bindingResult);

      return createModelAndView(variableDocument, Optional.of("addAnswerOptionButton"),
          windowYPosition);
    };
  }

  /**
   * save a valid variable document.
   * 
   * @return variableDetails.html or modify.html
   */
  @RequestMapping(method = RequestMethod.POST)
  public Callable<ModelAndView> save(@Valid VariableDocument variableDocument,
      BindingResult bindingResult, Integer windowYPosition) {
    return () -> {
      System.out.println(windowYPosition);
      if (!bindingResult.hasErrors()) {
        variableService.save(variableDocument);
        return new ModelAndView(
            "redirect:/" + LANGUAGES_PATH + VARIABLE_PATH + variableDocument.getId());
      } else {

        return createModelAndView(variableDocument, Optional.empty(), windowYPosition);
      }
    };
  }

  /**
   * Validate the given variable and return the result as json.
   * 
   * @param variableDocument The variable to be validated
   * @param bindingResult The result of the binding
   * @param locale The current locale
   * @return Return a Map fieldName->List of error messages
   */
  // VariableValidateController
  @RequestMapping(method = RequestMethod.POST, value = "/validate",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Callable<ValidationResultDto> validate(@Valid VariableDocument variableDocument,
      BindingResult bindingResult, Locale locale) {
    return () -> {
      ValidationResultDto validationResult = new ValidationResultDto();
      if (!bindingResult.hasErrors()) {
        // return empty map if there are no errors
        validationResult.setErrorMessageMap(new HashMap<String, List<String>>());
      } else {
        Map<String, List<String>> errorMessages = new HashMap<String, List<String>>();

        // convert all field errors into string map
        List<FieldError> allFieldErrors = bindingResult.getFieldErrors();
        for (FieldError error : allFieldErrors) {
          if (errorMessages.containsKey(error.getField())) {
            List<String> messages = errorMessages.get(error.getField());
            messages.add(messageSource.getMessage(error, locale));
          } else {
            List<String> messages = new ArrayList<String>();
            messages.add(messageSource.getMessage(error, locale));
            errorMessages.put(error.getField(), messages);
          }
        }
        // convert all global errors into string map
        List<ObjectError> allGlobalErrors = bindingResult.getGlobalErrors();
        for (ObjectError error : allGlobalErrors) {
          if (errorMessages.containsKey("global")) {
            List<String> messages = errorMessages.get("global");
            messages.add(messageSource.getMessage(error, locale));
          } else {
            List<String> messages = new ArrayList<String>();
            messages.add(messageSource.getMessage(error, locale));
            errorMessages.put("global", messages);
          }
        }
        validationResult.setErrorMessageMap(errorMessages);
      }
      return validationResult;
    };
  }

  protected abstract AbstractVariableModifyResource createResource(String variableId);
}
