package eu.dzhw.fdz.metadatamanagement.web.common.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.web.welcome.WelcomeController;

/**
 * This {@link ControllerAdvice} handles our custom Exceptions and displays them to the user.
 * 
 * @author Daniel Katzberg
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  private final ControllerLinkBuilderFactory controllerLinkBuilderFactory;

  private ExceptionLanguageUtils exceptionLanguageUtils;

  @Autowired
  public GlobalExceptionHandler(ControllerLinkBuilderFactory controllerLinkBuilderFactory) {
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
    this.exceptionLanguageUtils = new ExceptionLanguageUtils();
  }

  /**
   * Show the error view when a requested document cannot be found.
   * 
   * @param exception {@link DocumentNotFoundException}
   * @return the error view and its model
   */
  @ExceptionHandler(DocumentNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ModelAndView handleDocumentNotFoundException(DocumentNotFoundException exception) {
    // get the unknown id by the exception
    ExceptionPageResource notFoundResource = new ExceptionPageResource(exception,
        WelcomeController.class, this.controllerLinkBuilderFactory);

    // get language depending attribute names of the message property
    String navMessageLanguage =
        this.exceptionLanguageUtils.getCorrectReadableLanguage(exception.getLocale());
    String documentType =
        this.exceptionLanguageUtils.getCorrectDocumentType(exception.getDocumentClazz());

    // build model and view
    ModelAndView modelAndView = new ModelAndView("exceptionView");
    modelAndView.addObject("resource", notFoundResource);
    modelAndView.addObject("navMessageLanguage", navMessageLanguage);
    modelAndView.addObject("documentType", documentType);
    return modelAndView;
  }
}
