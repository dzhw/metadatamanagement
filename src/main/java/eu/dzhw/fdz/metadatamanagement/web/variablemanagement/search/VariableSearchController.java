package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.Bucket;
import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.PageWithBuckets;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.SearchValidationGroup.Search;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details.VariableResource;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details.VariableResourceAssembler;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.VariableSearchFilter;

/**
 * Controller for searching variables.
 * 
 * @author Amine Limouri
 * @author Daniel Katzberg
 */
@Controller
public class VariableSearchController {

  private VariableService variableService;
  private ControllerLinkBuilderFactory controllerLinkBuilderFactory;
  private VariableResourceAssembler variableResourceAssembler;
  private PagedResourcesAssembler<VariableDocument> pagedResourcesAssembler;

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
      PagedResourcesAssembler<VariableDocument> pagedResourcesAssembler) {
    this.variableService = variableService;
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
    this.variableResourceAssembler = variableResourceAssembler;
    this.pagedResourcesAssembler = pagedResourcesAssembler;
  }

  /**
   * Show variable search page. All request parameter are set at the searchFormDto. It includes the
   * parameter for the query and all filter/Aggregations.
   * 
   * @param ajaxHeader An ajaxheader with comes from a partial reload of the page. (search results
   *        returned by server)
   * @param variableSearchFilter the data tranfer object of the search form
   * @param pageable A pageable object for the
   * @param httpServletResponse A Servlet response from the server from the search
   * @return variableSearch.html
   */
  @RequestMapping(value = "/{language:de|en}/variables/search", method = RequestMethod.GET)
  public Callable<ModelAndView> get(
      @RequestHeader(name = "X-Requested-With", required = false) String ajaxHeader,
      @Validated(Search.class) VariableSearchFilter variableSearchFilter,
      BindingResult bindingResult, Pageable pageable,
      final HttpServletResponse httpServletResponse) {
    return () -> {
      ModelAndView modelAndView = new ModelAndView();
      modelAndView.addObject("variableSearchFilter", variableSearchFilter);
      PageWithBuckets<VariableDocument> pageableWithBuckets =
          this.variableService.search(variableSearchFilter, pageable);

      Map<String, Set<Bucket>> bucketMap = BucketManager
          .addEmptyBucketsIfNecessary(variableSearchFilter, pageableWithBuckets.getBucketMap());
      modelAndView.addObject("scaleLevelBuckets",
          bucketMap.get(VariableDocument.SCALE_LEVEL_FIELD.getPath()));
      modelAndView.addObject("surveyTitleBuckets",
          bucketMap.get(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD.getNestedPath()));
      // Create Resource
      PagedResources<VariableResource> pagedVariableResource = this.pagedResourcesAssembler
          .toResource(pageableWithBuckets, this.variableResourceAssembler);
      VariableSearchPageResource resource =
          new VariableSearchPageResource(pagedVariableResource, VariableSearchController.class,
              this.controllerLinkBuilderFactory, variableSearchFilter, pageable);
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
