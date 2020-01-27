package eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception;

/**
 * Exception which is to be thrown when bulk operations fail.
 * 
 * @author René Reitmann
 */
public class ElasticsearchBulkOperationException extends RuntimeException {

  private static final long serialVersionUID = 6529602377182254065L;

  public ElasticsearchBulkOperationException(Exception cause) {
    super("Bulk operation failed!", cause);
  }
}
