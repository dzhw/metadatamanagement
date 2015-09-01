package eu.dzhw.fdz.metadatamanagement.web.common.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

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

  // @Autowired
  // public DocumentNotFoundExceptionHandler(ReloadableResourceBundleMessageSource resourceBundle) {
  // super(resourceBundle);
  // }

  private final ControllerLinkBuilderFactory controllerLinkBuilderFactory;

  @Autowired
  public DocumentNotFoundExceptionHandler(
      ControllerLinkBuilderFactory controllerLinkBuilderFactory) {
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
  }

  @ExceptionHandler(DocumentNotFoundException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  ModelAndView handleDocumentNotFoundException(HttpServletRequest req,
      DocumentNotFoundException exception) throws Exception {

    // TODO DKatzberg: Remove or use?
    // return getResourceBundle().getMessage(
    // "typeMismatch.variableDocument.variableSurvey.surveyPeriod.startDate", null,
    // LocaleContextHolder.getLocale());

    ModelAndView modelAndView = new ModelAndView("/common/exception");
    modelAndView.addObject("exception", exception);
    modelAndView.addObject("url", req.getRequestURL());

    // TODO DKatzberg: Use WelcomeController -> Change!! Own get Method in this Handler or separated
    // Controller
    DocumentNotFoundResource notFoundResource = new DocumentNotFoundResource("TestID",
        WelcomeController.class, this.controllerLinkBuilderFactory);
    modelAndView.addObject("resource", notFoundResource);

    // TODO DKatzberg Not Localized
    modelAndView.addObject("errorMessage",
        "This is a not localized Errormessage: " + exception.getMessage());
    return modelAndView;
  }

}
