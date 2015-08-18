package eu.dzhw.fdz.metadatamanagement.config.binding;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;

/**
 * Register custom binding behavior for all controllers.
 * 
 * @author René Reitmann
 */
@ControllerAdvice
public class GlobalBindingInitializer {
  @InitBinder
  public void registerCustomEditors(WebDataBinder binder, WebRequest request) {
    // empty string coming from a form must be mapped to null
    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
  }
}
