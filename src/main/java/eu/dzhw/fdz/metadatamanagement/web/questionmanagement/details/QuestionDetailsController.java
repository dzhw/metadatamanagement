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
import eu.dzhw.fdz.metadatamanagement.web.common.search.AbstractDetailsController;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionVariable;

/**
 * The controller for the question detail page.
 * 
 * @author Daniel Katzberg
 * @author Amine Limouri
 */
@Controller
@RequestMapping(path = "/{language:de|en}/questions")
public class QuestionDetailsController extends
    AbstractDetailsController<QuestionDocument, QuestionService, 
    QuestionResource, QuestionResourceAssembler> {

  /**
   * The question variable resource assembler handels the links to the variables from the questions.
   */
  private QuestionVariableResourceAssembler questionVariableResourceAssembler;

  /**
   * Constructor for the controller of the question details page.
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
      QuestionVariableResourceAssembler questionVariableResourceAssembler) {
    super(questionService, controllerLinkBuilderFactory, questionResourceAssembler);
    this.questionVariableResourceAssembler = questionVariableResourceAssembler;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.web.common.search.AbstractDetailsController#get(java.lang.
   * String)
   */
  @Override
  @RequestMapping(path = "/{id}", method = RequestMethod.GET)
  public Callable<ModelAndView> get(@PathVariable("id") String id) {
    return () -> {
      // Empty list for the variable links from the questions details page.
      List<QuestionVariableResource> questionVariableResources =
          new ArrayList<QuestionVariableResource>();

      // Search for the question document.
      QuestionDocument questionDocument = this.service.get(id);

      // check for a valid found result
      if (questionDocument == null) {
        throw new DocumentNotFoundException(id, LocaleContextHolder.getLocale(),
            QuestionDocument.class);

        // Standard case: document was found
      } else {

        // create the links from the question to the variable ids
        for (Iterator<QuestionVariable> i = questionDocument.getQuestionVariables().iterator(); i
            .hasNext();) {
          questionVariableResources.add(questionVariableResourceAssembler.toResource(i.next()));
        }
        
        //build resources of the question
        QuestionResource questionResource =
            this.resourceAssembler.toResource(questionDocument);
        QuestionDetailsResource questionDetailsResource =
            new QuestionDetailsResource(controllerLinkBuilderFactory, questionResource);
        
        //build model and view
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("resource", questionDetailsResource);
        modelAndView.addObject("questionVariableResources", questionVariableResources);
        modelAndView.setViewName("questions/details");
        
        return modelAndView;
      }
    };
  }

}
