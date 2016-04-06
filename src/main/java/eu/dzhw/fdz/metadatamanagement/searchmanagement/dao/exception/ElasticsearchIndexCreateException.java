package eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception;

/**
 * Exception which is to be thrown if index creation fails.
 * 
 * @author René Reitmann
 */
public class ElasticsearchIndexCreateException extends RuntimeException {

  private static final long serialVersionUID = -5271442188138009839L;
  
  public ElasticsearchIndexCreateException(String index, String reason) {
    super("Unable to create index " + index + ": " + reason);
  }
}
