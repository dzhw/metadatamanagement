package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.filters.FilterManager;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.datatypes.PageWithBuckets;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details.VariableResource;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details.VariableResourceAssembler;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.SearchFormDto;

/**
 * Controller for searching variables.
 * 
 * @author Amine Limouri
 */
@Controller
public class VariableSearchController {

  private VariableService variableService;
  private ControllerLinkBuilderFactory controllerLinkBuilderFactory;
  private VariableResourceAssembler variableResourceAssembler;
  private PagedResourcesAssembler<VariableDocument> pagedResourcesAssembler;
  private ScaleLevelProvider scaleLevelProvider;

  /**
   * Create the controller.
   * 
   * @param variableService the service managing the variable state
   * @param controllerLinkBuilderFactory a factory for building links to resources
   * @param variableResourceAssembler to transform a VariableDocument into a VariableResource.
   * @param pagedResourcesAssembler to convert a Page instances into PagedResources.
   */
  @Autowired
  public VariableSearchController(VariableService variableService,
      ControllerLinkBuilderFactory controllerLinkBuilderFactory,
      VariableResourceAssembler variableResourceAssembler,
      PagedResourcesAssembler<VariableDocument> pagedResourcesAssembler,
      ScaleLevelProvider scaleLevelProvider) {
    this.variableService = variableService;
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
    this.variableResourceAssembler = variableResourceAssembler;
    this.pagedResourcesAssembler = pagedResourcesAssembler;
    this.scaleLevelProvider = scaleLevelProvider;
  }

  /**
   * Show variable search page.
   * 
   * @param ajaxHeader An ajaxheader with comes from a partial reload of the page. (search results
   *        returned by server)
   * @param searchFormDto the data tranfer object of the search form
   * @param pageable A pageable object for the
   * @param httpServletResponse A Servlet response from the server from the search
   * @return variableSearch.html
   */
  @RequestMapping(value = "/{language:de|en}/variables/search", method = RequestMethod.GET)
  public Callable<ModelAndView> get(
      @RequestHeader(name = "X-Requested-With", required = false) String ajaxHeader,
      SearchFormDto searchFormDto, Pageable pageable,
      final HttpServletResponse httpServletResponse) {
    return () -> {
      ModelAndView modelAndView = new ModelAndView();
      modelAndView.addObject("searchFormDto", searchFormDto);

      // add the filter manager to the model and view
      // the filter manager supports filter and save the status
      PageWithBuckets<VariableDocument> pageableAggregrationType =
          variableService.search(searchFormDto, pageable);
      FilterManager filterManager = new FilterManager(this.scaleLevelProvider, searchFormDto);
      filterManager.updateScaleLevelFilters(pageableAggregrationType.getBuckets());
      modelAndView.addObject("filterManager", filterManager);
      modelAndView.addObject("pageableAggregrationType", pageableAggregrationType);

      PagedResources<VariableResource> pagedVariableResource =
          pagedResourcesAssembler.toResource(pageableAggregrationType, variableResourceAssembler);
      VariableSearchPageResource resource = new VariableSearchPageResource(pagedVariableResource,
          VariableSearchController.class, controllerLinkBuilderFactory, searchFormDto, pageable);
      modelAndView.addObject("resource", resource);

      // Check for X-Requested-With Header
      // if not in the header, return the complete page
      String viewName = "variables/search";
      if (StringUtils.hasText(ajaxHeader)) {
        // if it is in the headers, return only a div
        viewName += " :: #searchResults";
      }

      modelAndView.setViewName(viewName);

      // disable caching of the search page to prevent displaying partial responses when
      // clicking the back button
      httpServletResponse.addHeader("Cache-Control",
          "no-cache, max-age=0, must-revalidate, no-store");

      return modelAndView;
    };
  }
}
