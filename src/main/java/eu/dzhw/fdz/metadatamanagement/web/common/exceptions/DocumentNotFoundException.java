package eu.dzhw.fdz.metadatamanagement.web.common.exceptions;

import java.util.Locale;

/**
 * The application is not in a valid state, if a document is not found. This exception will be
 * thrown in this case of matter.
 * 
 * @author Daniel Katzberg
 *
 */
public class DocumentNotFoundException extends IllegalStateException {

  /**
   * Default serial UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The unknown id which throws the exception.
   */
  private String unknownId;

  /**
   * The locale which throws the exception.
   */
  private Locale locale;

  /**
   * The class which throws the exception.
   */
  private String documentClazz;

  /**
   * The 'document not found' constructor needs three kinds of information: The id and class of a
   * document and a locale. It generates a message from this information and supports the super
   * constructor with a given String message.
   * 
   * @param unknownId The id of a document.
   * @param locale A given locale.
   * @param documentClazz The Class of the document.
   */
  public DocumentNotFoundException(String unknownId, Locale locale, String documentClazz) {
    super();
    this.unknownId = unknownId;
    this.locale = locale;
    this.documentClazz = documentClazz;
  }

  /* GETTER / SETTER */
  public String getUnknownId() {
    return unknownId;
  }

  public Locale getLocale() {
    return locale;
  }

  public String getDocumentClazz() {
    return documentClazz;
  }
}
