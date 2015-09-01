package eu.dzhw.fdz.metadatamanagement.web.common.exceptions;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * This is the abstract implementation of ExceptionHandler. The Handler holds an injected reference
 * to the ResourceBundle for internalization.
 * 
 * @author Daniel Katzberg
 *
 */
public abstract class AbstractExceptionHandler {

  /**
   * The resource bundle for the message properties.
   */
  private ReloadableResourceBundleMessageSource resourceBundle;

  /**
   * Constructor with a injected resource bundle.
   * 
   * @param resourceBundle The resource bundle is a reference to the message source.
   */
//  @Autowired
//  public AbstractExceptionHandler(ReloadableResourceBundleMessageSource resourceBundle) {
//    this.resourceBundle = resourceBundle;
//  }

  /* GETTER / SETTER */
  public ReloadableResourceBundleMessageSource getResourceBundle() {
    return resourceBundle;
  }

  public void setResourceBundle(ReloadableResourceBundleMessageSource resourceBundle) {
    this.resourceBundle = resourceBundle;
  }
}
