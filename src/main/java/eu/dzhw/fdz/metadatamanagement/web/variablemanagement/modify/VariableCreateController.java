package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import java.util.Optional;
import java.util.concurrent.Callable;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators.VariableDocumentCreateValidator;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;


/**
 * Controller used for creating variables with the variables/modify template.
 * 
 * @author René Reitmann
 */
@Controller
@RequestMapping(value = "/{language:de|en}/variables/create")
public class VariableCreateController extends AbstractVariableModifyController {

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
  public VariableCreateController(VariableService variableService,
      ControllerLinkBuilderFactory controllerLinkBuilderFactory,
      VariableDocumentCreateValidator validator, ScaleLevelProvider scaleLevelProvider,
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
  public Callable<ModelAndView> create(@Valid VariableDocument variableDocument,
      BindingResult bindingResult) {
    return () -> {
      return createModelAndView(variableDocument, Optional.empty());
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
    return new VariableCreateResource(controllerLinkBuilderFactory);
  }
}
