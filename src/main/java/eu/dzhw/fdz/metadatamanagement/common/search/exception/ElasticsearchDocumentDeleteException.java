package eu.dzhw.fdz.metadatamanagement.common.search.exception;

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
  
  public ElasticsearchDocumentDeleteException(String index, String type, String reason) {
    super("Unable to delete all documents of type " + type + " from index " + index
        + ": " + reason);
  }

  public ElasticsearchDocumentDeleteException(String index, String type, String fieldName,
      String value, String reason) {
    super("Unable to delete Document (field=" + fieldName + ", value=" + value 
        + ") of type " + type  + " from index " + index + ": " + reason);
  }
}
