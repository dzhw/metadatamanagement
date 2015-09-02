package eu.dzhw.fdz.metadatamanagement.web.common.exceptions;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

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
  public DocumentNotFoundExceptionHandler(ReloadableResourceBundleMessageSource resourceBundle,
      ControllerLinkBuilderFactory controllerLinkBuilderFactory) {
    super(resourceBundle);
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
  }

  @ExceptionHandler(DocumentNotFoundException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  ModelAndView handleDocumentNotFoundException(HttpServletRequest req,
      DocumentNotFoundException exception) throws Exception {

    String[] args = {exception.getDocumentClazz(), exception.getUnknownId(),
        exception.getLocale().toString()};
    String message = getResourceBundle().getMessage("error.documentNotFoundException.message", args,
        LocaleContextHolder.getLocale());

    ModelAndView modelAndView = new ModelAndView("/common/exception");
    modelAndView.addObject("exception", exception);
    modelAndView.addObject("url", req.getRequestURL());

    // get the unknown id by the exception
    DocumentNotFoundResource notFoundResource =
        new DocumentNotFoundResource(exception.getUnknownId(),
            DocumentNotFoundExceptionHandler.class, this.controllerLinkBuilderFactory);
    modelAndView.addObject("resource", notFoundResource);
    modelAndView.addObject("errorMessage", message);
    return modelAndView;
  }

  /**
   * Language Support for error pages.
   * 
   * @return empty model and view object for language support
   */
  // TODO Question: Is there a better workaround?
  @RequestMapping(value = "/{language:de|en}/variables/{unknownId}/edit",
      method = RequestMethod.GET)
  public Callable<ModelAndView> get(@PathVariable("unknownId") String unknownId) {
    return () -> {
      return new ModelAndView();
    };
  }

}
