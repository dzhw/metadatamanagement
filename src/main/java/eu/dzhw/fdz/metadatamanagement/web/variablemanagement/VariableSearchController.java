package eu.dzhw.fdz.metadatamanagement.web.variablemanagement;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.resources.VariableResource;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.resources.VariableResourceAssembler;

/**
 * Controller for searching variables.
 * 
 * @author Amine Limouri
 */
@Controller
@RequestMapping(value = "/{language}/variables/search")
public class VariableSearchController {

  private VariableService variableService;
  private ControllerLinkBuilderFactory controllerLinkBuilderFactory;
  private VariableResourceAssembler variableResourceAssembler;
  private PagedResourcesAssembler<VariableDocument> pagedResourcesAssembler;

  /**
   * Autowire needed objects.
   * 
   * @param variableService his is a case with description
   * @param controllerLinkBuilderFactory his is a case with description
   * @param variableResourceAssembler his is a case with description
   * @param pagedResourcesAssembler his is a case with description
   */
  @Autowired
  public VariableSearchController(VariableService variableService,
      ControllerLinkBuilderFactory controllerLinkBuilderFactory,
      VariableResourceAssembler variableResourceAssembler,
      PagedResourcesAssembler<VariableDocument> pagedResourcesAssembler) {
    this.variableService = variableService;
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
    this.variableResourceAssembler = variableResourceAssembler;
    this.pagedResourcesAssembler = pagedResourcesAssembler;
  }

  /**
   * Show variable search page.
   * 
   * @return variableSearch.html
   */
  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView get(@RequestParam(required = false) String query, Pageable pageable) {

    ModelAndView modelAndView = new ModelAndView("variables/variableSearch");
    Page<VariableDocument> variableDocument = variableService.search(query, pageable);
    PagedResources<VariableResource> pagedVariableResource =
        pagedResourcesAssembler.toResource(variableDocument, variableResourceAssembler);

    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      pagedVariableResource.add(controllerLinkBuilderFactory.linkTo(
          methodOn(VariableSearchController.class, supportedLocale).get(query, pageable)).withRel(
          supportedLocale.getLanguage()));
    }

    System.out.println(pagedVariableResource.getContent().size());
    modelAndView.addObject("query", query);
    modelAndView.addObject("resource", pagedVariableResource);
    return modelAndView;
  }
}
