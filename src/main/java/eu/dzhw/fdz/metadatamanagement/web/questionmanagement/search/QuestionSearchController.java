package eu.dzhw.fdz.metadatamanagement.web.questionmanagement.search;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.SearchValidationGroup.Search;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionDocument;
import eu.dzhw.fdz.metadatamanagement.service.questionmanagement.QuestionService;
import eu.dzhw.fdz.metadatamanagement.web.common.search.AbstractSearchController;
import eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details.QuestionResource;
import eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details.QuestionResourceAssembler;
import eu.dzhw.fdz.metadatamanagement.web.questionmanagement.search.dto.QuestionSearchFilter;

/**
 * Controller for searching questions.
 * 
 * @author Daniel Katzberg
 *
 */
@Controller
public class QuestionSearchController
    extends AbstractSearchController<QuestionDocument, QuestionResource, QuestionSearchFilter> {

  /**
   * Create the controller.
   * 
   * @param questionService the service managing the question repository calls
   * @param controllerLinkBuilderFactory a factory for building links to resources
   * @param questionResourceAssembler to transform a VariableDocument into a VariableResource.
   * @param pagedResourcesAssembler to convert a Page instances into PagedResources.
   */
  @Autowired
  public QuestionSearchController(QuestionService questionService,
      ControllerLinkBuilderFactory controllerLinkBuilderFactory,
      QuestionResourceAssembler questionResourceAssembler,
      PagedResourcesAssembler<QuestionDocument> pagedResourcesAssembler) {
    super(controllerLinkBuilderFactory, questionResourceAssembler, pagedResourcesAssembler,
        questionService);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.web.common.search.AbstractSearchController#get(java.lang.String,
   * eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter,
   * org.springframework.data.domain.Pageable, javax.servlet.http.HttpServletResponse)
   */
  @Override
  @RequestMapping(value = "/{language:de|en}/questions/search", method = RequestMethod.GET)
  public Callable<ModelAndView> get(
      @RequestHeader(name = "X-Requested-With", required = false) String ajaxHeader,
      @Validated(Search.class) QuestionSearchFilter searchFilter, Pageable pageable,
      HttpServletResponse httpServletResponse) {

    return () -> {

      // Create facedpage (from search by service)
      ModelAndView modelAndView = new ModelAndView();
      modelAndView.addObject("questionSearchFilter", searchFilter);
      FacetedPage<QuestionDocument> pageableDocument =
          this.searchService.search(searchFilter, pageable);

      // Create Resource
      PagedResources<QuestionResource> pagedQuestionResource =
          this.pagedResourcesAssembler.toResource(pageableDocument, this.resourceAssembler);
      QuestionSearchResource resource =
          new QuestionSearchResource(pagedQuestionResource, QuestionSearchController.class,
              this.controllerLinkBuilderFactory, searchFilter, pageable);
      modelAndView.addObject("resource", resource);

      // Check for X-Requested-With Header
      // if not in the header, return the complete page
      String viewName = "questions/search";
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
