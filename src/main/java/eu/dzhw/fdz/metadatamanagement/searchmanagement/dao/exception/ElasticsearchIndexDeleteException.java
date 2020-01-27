package eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception;

/**
 * Exception which is to be thrown when index deletion fails.
 * 
 * @author René Reitmann
 */
public class ElasticsearchIndexDeleteException extends RuntimeException {

  private static final long serialVersionUID = 5414412678405433039L;
  
  public ElasticsearchIndexDeleteException(String index, Exception cause) {
    super("Unable to delete search index " + index + ".", cause);
  }
}