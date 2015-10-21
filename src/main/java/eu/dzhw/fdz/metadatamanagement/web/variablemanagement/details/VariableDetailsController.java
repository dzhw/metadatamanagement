package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.common.exceptions.DocumentNotFoundException;
import eu.dzhw.fdz.metadatamanagement.web.common.search.AbstractDetailsController;

/**
 * A Controller which returns a details page for a variable or error a page.
 * 
 * @author Amine limouri
 * @author Daniel Katzberg
 */
@Controller
@RequestMapping(path = "/{language:de|en}/variables")
public class VariableDetailsController extends
    AbstractDetailsController<VariableDocument, VariableService, 
    VariableResource, VariableResourceAssembler> {

  /**
   * Create the controller.
   * 
   * @param variableService the service managing the variable state
   * @param controllerLinkBuilderFactory a factory for building links to resources
   * @param variableResourceAssembler to transform a VariableDocument into a VariableResource.
   */
  @Autowired
  public VariableDetailsController(VariableService variableService,
      ControllerLinkBuilderFactory controllerLinkBuilderFactory,
      VariableResourceAssembler variableResourceAssembler) {
    super(variableService, controllerLinkBuilderFactory, variableResourceAssembler);
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.web.common.search.AbstractDetailsController#get(java.lang.
   * String)
   */
  @Override
  @RequestMapping(path = "/{id}", method = RequestMethod.GET)
  public Callable<ModelAndView> get(@PathVariable("id") String id) {
    return () -> {

      // Get VariableDocument
      VariableDocument variableDocument = this.service.get(id);
      
      //check for valid result
      if (variableDocument == null) {
        throw new DocumentNotFoundException(id, LocaleContextHolder.getLocale(),
            VariableDocument.class);
        
        //standard case: document was found, prepare model and view
      } else {
        
        //Create Resources
        VariableResource variableResource = this.resourceAssembler.toResource(variableDocument);
        VariableDetailsResource resource =
            new VariableDetailsResource(controllerLinkBuilderFactory, variableResource);
        
        //Build model and view
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("resource", resource);
        modelAndView.setViewName("variables/details");
        
        return modelAndView;
      }
    };
  }
}
