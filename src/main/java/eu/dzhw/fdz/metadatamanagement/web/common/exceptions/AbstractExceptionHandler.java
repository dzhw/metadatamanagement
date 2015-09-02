package eu.dzhw.fdz.metadatamanagement.web.common.exceptions;

import eu.dzhw.fdz.metadatamanagement.web.common.exceptions.utils.ExceptionLanguageUtils;

/**
 * This is the abstract implementation of ExceptionHandler.
 * 
 * @author Daniel Katzberg
 *
 */
public abstract class AbstractExceptionHandler {

  private ExceptionLanguageUtils exceptionLanguageUtils;

  /**
   * A basic constructor for the abstract exception handler.
   */
  public AbstractExceptionHandler() {
    this.exceptionLanguageUtils = new ExceptionLanguageUtils();
  }


  /* GETTER / SETTER */
  public ExceptionLanguageUtils getExceptionLanguageUtils() {
    return exceptionLanguageUtils;
  }
}
