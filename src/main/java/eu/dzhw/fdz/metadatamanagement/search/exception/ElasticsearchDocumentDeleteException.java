package eu.dzhw.fdz.metadatamanagement.search.exception;

/**
 * Exception which is to be thrown when deletion of a search document fails.
 * 
 * @author René Reitmann
 */
public class ElasticsearchDocumentDeleteException extends RuntimeException {

  private static final long serialVersionUID = -7305092127622993072L;

  public ElasticsearchDocumentDeleteException(String index, String type, String id, String reason) {
    super("Unable to delete Document (ID=" + id + ") of type " + type + " from index " + index
        + ": " + reason);
  }
}
