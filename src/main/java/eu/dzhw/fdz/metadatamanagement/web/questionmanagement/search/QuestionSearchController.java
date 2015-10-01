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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.SearchValidationGroup.Search;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionDocument;
import eu.dzhw.fdz.metadatamanagement.service.questionmanagement.QuestionService;
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
public class QuestionSearchController {

  private QuestionService questionService;
  private ControllerLinkBuilderFactory controllerLinkBuilderFactory;
  private QuestionResourceAssembler questionResourceAssembler;
  private PagedResourcesAssembler<QuestionDocument> pagedResourcesAssembler;

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
    this.questionService = questionService;
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
    this.questionResourceAssembler = questionResourceAssembler;
    this.pagedResourcesAssembler = pagedResourcesAssembler;
  }

  /**
   * Show question search page. It includes the parameter for the query..
   * 
   * @param ajaxHeader An ajaxheader with comes from a partial reload of the page. (search results
   *        returned by server)
   * @param pageable A pageable object for the
   * @return questionSearch.html
   */
  @RequestMapping(value = "/{language:de|en}/questions/search", method = RequestMethod.GET)
  public Callable<ModelAndView> get(
      @RequestHeader(name = "X-Requested-With", required = false) String ajaxHeader,
      @Validated(Search.class) QuestionSearchFilter questionSearchFilter,
      BindingResult bindingResult, Pageable pageable, HttpServletResponse httpServletResponse) {

    return () -> {

      // Create pageableWithBuckets (from search by service)
      ModelAndView modelAndView = new ModelAndView();
      modelAndView.addObject("questionSearchFilter", questionSearchFilter);
      FacetedPage<QuestionDocument> pageableDocument =
          this.questionService.search(questionSearchFilter, pageable);

      // Create Resource
      PagedResources<QuestionResource> pagedQuestionResource = this.pagedResourcesAssembler
          .toResource(pageableDocument, this.questionResourceAssembler);
      QuestionSearchResource resource =
          new QuestionSearchResource(pagedQuestionResource, QuestionSearchController.class,
              this.controllerLinkBuilderFactory, questionSearchFilter, pageable);
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
