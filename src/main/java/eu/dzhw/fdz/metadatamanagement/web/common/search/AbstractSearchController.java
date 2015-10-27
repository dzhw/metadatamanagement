package eu.dzhw.fdz.metadatamanagement.web.common.search;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.SearchValidationGroup.Search;
import eu.dzhw.fdz.metadatamanagement.service.common.SearchService;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.SearchFilter;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.SuggestDto;

/**
 * This class manage some basic functions for the different search controller is the abstract class
 * for them.
 * 
 * @param <D> Class of the Document
 * @param <R> Class of the Resource
 * @param <F> Class of the Filter
 * 
 * @author Daniel Katzberg
 *
 */
public abstract class AbstractSearchController<D extends AbstractDocument, 
    R extends ResourceSupport, F extends SearchFilter> {

  protected ControllerLinkBuilderFactory controllerLinkBuilderFactory;
  protected ResourceAssemblerSupport<D, R> resourceAssembler;
  protected PagedResourcesAssembler<D> pagedResourcesAssembler;
  protected SearchService<D> searchService;


  /**
   * Default constructor for the abstract search controller.
   * 
   * @param controllerLinkBuilderFactory A builder factory for all links
   * @param resourceAssembler the assembler for creation of resources from documents
   * @param pagedResourcesAssembler the paged resource assembler for the paged resource
   * @param searchService the service class for the repository methods.
   */
  public AbstractSearchController(ControllerLinkBuilderFactory controllerLinkBuilderFactory,
      ResourceAssemblerSupport<D, R> resourceAssembler,
      PagedResourcesAssembler<D> pagedResourcesAssembler, SearchService<D> searchService) {
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
    this.resourceAssembler = resourceAssembler;
    this.pagedResourcesAssembler = pagedResourcesAssembler;
    this.searchService = searchService;
  }


  /**
   * Show a search page. All request parameter are set at the searchFormDto. It includes the
   * parameter for the query and all filter/Aggregations.
   * 
   * @param ajaxHeader An ajaxheader with comes from a partial reload of the page. (search results
   *        returned by server)
   * @param searchFilter the data tranfer object of the search form
   * @param pageable A pageable object for the
   * @param httpServletResponse the reponse is needed for deactivation of the browser cache (search
   *        as you type)
   * @return a search.html
   */
  public abstract Callable<ModelAndView> get(
      @RequestHeader(name = "X-Requested-With", required = false) String ajaxHeader,
      @Validated(Search.class) F searchFilter, BindingResult bindingResult,
      Pageable pageable, HttpServletResponse httpServletResponse);
  
  /**
   * Return search suggestions for the given input.
   * 
   * @param term The query term as given by the user.
   * @return A List of suggestions.
   */
  public abstract Callable<SuggestDto> suggest(String term);

  /**
   * Check for X-Requested-With Header. If not in the header, return the complete page
   * 
   * @param viewName Is the subpath of the url. e.g. variables/search
   * @param ajaxHeader The ajaxheader. If it is not set, it
   * @param modelAndView The model and view object of the request.
   * @return the updated model and view
   */
  protected ModelAndView checkHeader(String viewName, String ajaxHeader,
      ModelAndView modelAndView) {
    if (StringUtils.hasText(ajaxHeader)) {
      // if it is in the headers, return only a div
      viewName += " :: #searchResults";
    }
    modelAndView.setViewName(viewName);

    return modelAndView;
  }

  /**
   * Disable caching of the search page to prevent displaying partial responses when clicking the
   * back button.
   */
  protected void disableCacheAtBrowser(HttpServletResponse httpServletResponse) {
    httpServletResponse.addHeader("Cache-Control",
        "no-cache, max-age=0, must-revalidate, no-store");
  }
}
