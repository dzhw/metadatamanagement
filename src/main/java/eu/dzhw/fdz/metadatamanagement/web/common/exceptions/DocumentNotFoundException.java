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
   * The 'document not found' constructor needs three kinds of information: The id and class of a
   * document and a locale. It generates a message from this information and supports the super
   * constructor with a given String message.
   * 
   * @param documentId The id of a document.
   * @param locale A given locale.
   * @param documentClazz The Class of the document.
   */
  public DocumentNotFoundException(String documentId, Locale locale, String documentClazz) {
    super(documentClazz + " with ID" + documentId + " (" + locale + ") not found!");
  }
}
