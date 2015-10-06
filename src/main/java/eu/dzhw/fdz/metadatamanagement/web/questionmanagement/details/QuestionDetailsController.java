package eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details;

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

import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionDocument;
import eu.dzhw.fdz.metadatamanagement.service.questionmanagement.QuestionService;
import eu.dzhw.fdz.metadatamanagement.web.common.exceptions.DocumentNotFoundException;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionVariable;
/**
 * The controller for the question detail page.
 * 
 * @author Daniel Katzberg
 * @author Amine Limouri
 */
@Controller
@RequestMapping(path = "/{language:de|en}/questions")
public class QuestionDetailsController {

  private QuestionService questionService;
  private ControllerLinkBuilderFactory controllerLinkBuilderFactory;
  private QuestionResourceAssembler questionResourceAssembler;
  private QuestionVariableResourceAssembler questionVariableResourceAssembler;

  /**
   * Constructor for the controller of the details page.
   * 
   * @param questionService the service class for sending queries to the repository.
   * @param controllerLinkBuilderFactory factory for building links.
   * @param questionResourceAssembler the resource assembler for questions
   * @param questionVariableResourceAssembler the resource assembler for questionVariables
   * 
   */
  @Autowired
  public QuestionDetailsController(QuestionService questionService,
      ControllerLinkBuilderFactory controllerLinkBuilderFactory,
      QuestionResourceAssembler questionResourceAssembler,
      QuestionVariableResourceAssembler questionVariableResourceAssembler
  ) {
    this.questionService = questionService;
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
    this.questionResourceAssembler = questionResourceAssembler;
    this.questionVariableResourceAssembler = questionVariableResourceAssembler;
  }

  /**
   * return the details or throw exception.
   * 
   * @return details.html
   */
 /* @RequestMapping(path = "/{questionId}", method = RequestMethod.GET)
  @ResponseBody
  public Callable<QuestionDetailsResource> get(@PathVariable("questionId") String questionId) {
    return () -> {
      QuestionDocument questionDocument = this.questionService.get(questionId);
      if (questionDocument == null) {
        throw new DocumentNotFoundException(questionId, LocaleContextHolder.getLocale(),
            QuestionDocument.class);
      } else {
        QuestionResource questionResource =
            this.questionResourceAssembler.toResource(questionDocument);
        QuestionDetailsResource questionDetailsResource =
            new QuestionDetailsResource(controllerLinkBuilderFactory, questionResource);
        return questionDetailsResource;
      }
    };
  }*/
  
  /**
   * return the details or throw exception.
   * 
   * @return details.html
   */
  @RequestMapping(path = "/{questionId}", method = RequestMethod.GET)
  public Callable<ModelAndView> get(@PathVariable("questionId") String questionId) {
    return () -> {
      List<QuestionVariableResource> variableResources = new ArrayList<QuestionVariableResource>();
      QuestionDocument questionDocument = this.questionService.get(questionId);
      if (questionDocument == null) {
        throw new DocumentNotFoundException(questionId, LocaleContextHolder.getLocale(),
            QuestionDocument.class);
      } else {
        for ( Iterator<QuestionVariable> i =  questionDocument.getQuestionVariables().iterator(); 
            i.hasNext(); ) {
          variableResources.add(questionVariableResourceAssembler.toResource(i.next()));
 
        }
        ModelAndView modelAndView = new ModelAndView();
        QuestionResource questionResource =
            this.questionResourceAssembler.toResource(questionDocument);
        QuestionDetailsResource questionDetailsResource =
            new QuestionDetailsResource(controllerLinkBuilderFactory, questionResource);
        modelAndView.addObject("resource", questionDetailsResource);
        modelAndView.addObject("variableResources", variableResources);
        modelAndView.setViewName("questions/details");
        return modelAndView;
      }
    };
  }

}
