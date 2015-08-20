package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.VariableDocumentValidator;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.ValidationResultDto;

/**
 * Rest controller which validates variables.
 * 
 * @author René Reitmann
 */
@RestController
@RequestMapping(value = "/{language:de|en}/variables/validate")
public class VariableValidateController {
  private VariableDocumentValidator validator;
  private MessageSource messageSource;

  /**
   * Create the controller with the {@link VariableDocumentValidator} and a {@link MessageSource}
   * for I18n.
   */
  @Autowired
  public VariableValidateController(VariableDocumentValidator validator,
      MessageSource messageSource) {
    this.validator = validator;
    this.messageSource = messageSource;
  }

  @InitBinder("variableDocument")
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(validator);
  }

  /**
   * Validate the given variable and return the result as json.
   * 
   * @param variableDocument The variable to be validated
   * @param bindingResult The result of the binding
   * @param locale The current locale
   * @return Return a Map fieldName->List of error messages
   */
  @RequestMapping(method = RequestMethod.POST)
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
}
