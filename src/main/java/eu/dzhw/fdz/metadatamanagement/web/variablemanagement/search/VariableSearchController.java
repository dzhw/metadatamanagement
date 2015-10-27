package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.Bucket;
import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.PageWithBuckets;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.DocumentField;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.SearchValidationGroup.Search;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.BucketManager;
import eu.dzhw.fdz.metadatamanagement.web.common.search.AbstractSearchController;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details.VariableResource;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details.VariableResourceAssembler;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.SuggestDto;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.VariableSearchFilter;

/**
 * Controller for searching variables.
 * 
 * @author Amine Limouri
 * @author Daniel Katzberg
 */
@Controller
public class VariableSearchController
    extends AbstractSearchController<VariableDocument, VariableResource, VariableSearchFilter> {
  
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
    super(controllerLinkBuilderFactory, variableResourceAssembler, pagedResourcesAssembler,
        variableService);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.web.common.search.AbstractSearchController#get(java.lang.String,
   * eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter,
   * org.springframework.data.domain.Pageable, javax.servlet.http.HttpServletResponse)
   */

  @RequestMapping(value = "/{language:de|en}/variables/search", method = RequestMethod.GET)
  @Override
  public Callable<ModelAndView> get(
      @RequestHeader(name = "X-Requested-With", required = false) String ajaxHeader,
      @Validated(Search.class) VariableSearchFilter variableSearchFilter,
      BindingResult bindingResult,
      Pageable pageable, HttpServletResponse httpServletResponse) {
    return () -> {

      // Create pageableWithBuckets (from search by service)
      ModelAndView modelAndView = new ModelAndView();

      // Search
      PageWithBuckets<VariableDocument> pageableWithBuckets =
          this.searchService.search(variableSearchFilter, pageable);

      // Add Buckets to Model and view
      Map<DocumentField, Set<Bucket>> bucketMap = BucketManager
          .addEmptyBucketsIfNecessary(variableSearchFilter, pageableWithBuckets.getBucketMap());
      modelAndView.addObject("scaleLevelBuckets", bucketMap.get(VariableDocument.SCALE_LEVEL_FIELD)
          .stream().sorted().collect(Collectors.toList()));
      modelAndView.addObject("surveyTitleBuckets",
          bucketMap.get(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD).stream().sorted()
              .collect(Collectors.toList()));

      // Create Resources and add the search resource to the model
      PagedResources<VariableResource> pagedVariableResource =
          this.pagedResourcesAssembler.toResource(pageableWithBuckets, this.resourceAssembler);
      VariableSearchPageResource resource =
          new VariableSearchPageResource(pagedVariableResource, VariableSearchController.class,
              this.controllerLinkBuilderFactory, variableSearchFilter, pageable);
      modelAndView.addObject("resource", resource);
      modelAndView.addObject("variableSearchFilter", variableSearchFilter);
      
      // Check for ajax header.
      modelAndView = this.checkHeader("variables/search", ajaxHeader, modelAndView);

      // Disable Cache
      this.disableCacheAtBrowser(httpServletResponse);

      return modelAndView;
    };
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.web.common.search.AbstractSearchController#suggest(java.lang.
   * String)
   */
  @RequestMapping(value = "/{language:de|en}/variables/search/suggest", method = RequestMethod.GET)
  @ResponseBody
  @Override
  public Callable<SuggestDto> suggest(String term) {
    return () -> {
      return new SuggestDto(((VariableService) this.searchService).suggest(term));
    };
  }
}
