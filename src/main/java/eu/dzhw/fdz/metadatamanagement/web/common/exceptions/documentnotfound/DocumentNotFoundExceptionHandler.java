package eu.dzhw.fdz.metadatamanagement.web.common.exceptions.documentnotfound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.web.common.exceptions.AbstractExceptionHandler;
import eu.dzhw.fdz.metadatamanagement.web.welcome.WelcomeController;

/**
 * The document not found exception handler handles document not found over the whole application,
 * if this exception will be thrown anywhere. This is an advice controller. That means that the
 * controller supports all other controller and handles the exception.
 * 
 * @author Daniel Katzberg
 *
 */
@ControllerAdvice
public class DocumentNotFoundExceptionHandler extends AbstractExceptionHandler {

  private final ControllerLinkBuilderFactory controllerLinkBuilderFactory;

  @Autowired
  public DocumentNotFoundExceptionHandler(
      ControllerLinkBuilderFactory controllerLinkBuilderFactory) {
    super();
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
  }

  @ExceptionHandler(DocumentNotFoundException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  ModelAndView handleDocumentNotFoundException(DocumentNotFoundException exception)
      throws Exception {



    // get the unknown id by the exception
    DocumentNotFoundResource notFoundResource = new DocumentNotFoundResource(exception,
        WelcomeController.class, this.controllerLinkBuilderFactory);

    // get language depending attribute names of the message property
    String navMessageLanguage =
        this.getExceptionLanguageUtils().getCorrectReadableLanguage(exception.getLocale());
    String documentType =
        this.getExceptionLanguageUtils().getCorrectDocumentType(exception.getDocumentClazz());

    // build model and view
    ModelAndView modelAndView = new ModelAndView("/common/exceptionAlert");
    modelAndView.addObject("exception", exception);
    modelAndView.addObject("resource", notFoundResource);
    modelAndView.addObject("navMessageLanguage", navMessageLanguage);
    modelAndView.addObject("documentType", documentType);
    return modelAndView;
  }
}
