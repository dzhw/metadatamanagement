package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import java.util.Optional;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators.VariableDocumentEditValidator;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.common.exceptions.DocumentNotFoundException;

/**
 * Controller used for editing variables with the variables/modify template.
 * 
 * @author René Reitmann
 */
@Controller
@RequestMapping(value = "/{language:de|en}/variables/{variableId}/edit")
public class VariableEditController extends AbstractVariableModifyController {
  /**
   * Create the controller.
   * 
   * @param variableService the service managing the variable state
   * @param controllerLinkBuilderFactory a factory for building links to resources
   * @param validator the validator for variables
   * @param scaleLevelProvider a provider returning valid scalelevels
   * @param dataTypesProvider a provider returning valid datatypes
   */
  @Autowired
  public VariableEditController(VariableService variableService,
      ControllerLinkBuilderFactory controllerLinkBuilderFactory,
      VariableDocumentEditValidator validator, ScaleLevelProvider scaleLevelProvider,
      DataTypesProvider dataTypesProvider) {
    super(variableService, controllerLinkBuilderFactory, validator, scaleLevelProvider,
        dataTypesProvider);
  }

  /**
   * Create a variable.
   * 
   * @return modify.html
   */
  @RequestMapping(method = RequestMethod.GET)
  public Callable<ModelAndView> edit(@PathVariable("variableId") String variableId) {
    return () -> {
      VariableDocument variableDocument = variableService.get(variableId);
      if (variableDocument == null) {
        throw new DocumentNotFoundException(variableId, LocaleContextHolder.getLocale(),
            VariableDocument.class.getSimpleName());
      }
      BeanPropertyBindingResult errors =
          new BeanPropertyBindingResult(variableDocument, "variableDocument");
      validator.validate(variableDocument, errors);
      ModelAndView modelAndView = createModelAndView(variableDocument, Optional.empty(), 0);
      modelAndView.addAllObjects(errors.getModel());
      return modelAndView;
    };
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify.AbstractVariableModifyController#
   * createResource(java.lang.String)
   */
  @Override
  protected AbstractVariableModifyResource createResource(String variableId) {
    return new VariableEditResource(controllerLinkBuilderFactory, variableId);
  }
}
