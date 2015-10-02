package eu.dzhw.fdz.metadatamanagement.web.common.search;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.SearchValidationGroup.Search;
import eu.dzhw.fdz.metadatamanagement.service.common.SearchService;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter;

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
public abstract class AbstractSearchController
    <D extends AbstractDocument, R extends ResourceSupport, F extends AbstractSearchFilter> {

  protected ControllerLinkBuilderFactory controllerLinkBuilderFactory;
  protected ResourceAssemblerSupport<D, R> resourceAssembler;
  protected PagedResourcesAssembler<D> pagedResourcesAssembler;
  protected SearchService<D> searchService;


  /**
   * Yeah. TODO
   * 
   * @param controllerLinkBuilderFactory Yeah.
   * @param resourceAssembler Yeah.
   * @param pagedResourcesAssembler Yeah.
   * @param searchService Yeah.
   */
  public AbstractSearchController(ControllerLinkBuilderFactory controllerLinkBuilderFactory,
      ResourceAssemblerSupport<D, R> resourceAssembler,
      PagedResourcesAssembler<D> pagedResourcesAssembler,
      SearchService<D> searchService) {
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
      @Validated(Search.class) F searchFilter, Pageable pageable,
      HttpServletResponse httpServletResponse);
}
