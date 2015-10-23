package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.RelatedVariable;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.common.RelatedVariableResource;
import eu.dzhw.fdz.metadatamanagement.web.common.RelatedVariableResourceAssembler;
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
   * The variable resource assembler handels the links to the variables which were the base of a
   * generated variable..
   */
  private RelatedVariableResourceAssembler relatedVariableResourceAssembler;

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
      VariableResourceAssembler variableResourceAssembler,
      RelatedVariableResourceAssembler relatedVariableResourceAssembler) {
    super(variableService, controllerLinkBuilderFactory, variableResourceAssembler);
    this.relatedVariableResourceAssembler = relatedVariableResourceAssembler;
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

      // Empty list for the variable links from the questions details page.
      List<RelatedVariableResource> relatedVariableResources =
          new ArrayList<RelatedVariableResource>();

      // Get VariableDocument
      VariableDocument variableDocument = this.service.get(id);

      // check for valid result
      if (variableDocument == null) {
        throw new DocumentNotFoundException(id, LocaleContextHolder.getLocale(),
            VariableDocument.class);

        // standard case: document was found, prepare model and view
      } else {

        // Create Resources
        VariableResource variableResource = this.resourceAssembler.toResource(variableDocument);
        VariableDetailsResource resource =
            new VariableDetailsResource(controllerLinkBuilderFactory, variableResource);

        // create the links from the question to the variable ids
        for (Iterator<RelatedVariable> i = variableDocument.getRelatedVariables().iterator(); i
            .hasNext();) {
          relatedVariableResources.add(relatedVariableResourceAssembler.toResource(i.next()));
        }

        // Build model and view
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("resource", resource);
        modelAndView.addObject("relatedVariableResources", relatedVariableResources);
        modelAndView.setViewName("variables/details");

        return modelAndView;
      }
    };
  }
}
