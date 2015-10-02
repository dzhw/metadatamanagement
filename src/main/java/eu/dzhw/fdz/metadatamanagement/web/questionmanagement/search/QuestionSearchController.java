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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.SearchValidationGroup.Search;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionDocument;
import eu.dzhw.fdz.metadatamanagement.service.questionmanagement.QuestionService;
import eu.dzhw.fdz.metadatamanagement.web.common.search.AbstractSearchController;
import eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details.QuestionResource;
import eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details.QuestionResourceAssembler;
import eu.dzhw.fdz.metadatamanagement.web.questionmanagement.search.dto.QuestionSearchFilter;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.SuggestDto;

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
      modelAndView.addObject("searchFilter", searchFilter);
      FacetedPage<QuestionDocument> pageableDocument =
          this.searchService.search(searchFilter, pageable);

      // Create Resource
      PagedResources<QuestionResource> pagedQuestionResource =
          this.pagedResourcesAssembler.toResource(pageableDocument, this.resourceAssembler);
      QuestionSearchResource resource =
          new QuestionSearchResource(pagedQuestionResource, QuestionSearchController.class,
              this.controllerLinkBuilderFactory, searchFilter, pageable);
      modelAndView.addObject("resource", resource);

      //Check for ajax header.
      modelAndView = this.checkHeader("questions/search", ajaxHeader, modelAndView);

      //Disable Cache
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
  @RequestMapping(value = "/{language:de|en}/questions/search/suggest", method = RequestMethod.GET)
  @ResponseBody
  @Override
  public Callable<SuggestDto> suggest(String term) {
    //TODO implement suggest support for questions
    return null;
  }
}
