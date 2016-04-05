package eu.dzhw.fdz.metadatamanagement.common.search.exception;

/**
 * Exception which is to be thrown when saving of search documents fails.
 * 
 * @author René Reitmann
 */
public class ElasticsearchDocumentSaveException extends RuntimeException {

  private static final long serialVersionUID = -5977365400505688245L;

  public ElasticsearchDocumentSaveException(String index, String type, String id, String reason) {
    super("Unable to save document (ID=" + id + ") of type " + type + " to index " + index + ": "
        + reason);
  }

  public ElasticsearchDocumentSaveException(String index, String type, String reason) {
    super("Unable to bulk save documents of type " + type + " to index " + index + ": " + reason);
  }
}
