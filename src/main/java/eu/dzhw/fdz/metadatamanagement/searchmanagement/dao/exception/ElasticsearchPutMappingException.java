package eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception;

/**
 * Exception which is to be thrown if mapping creation fails.
 * 
 * @author René Reitmann
 */
public class ElasticsearchPutMappingException extends RuntimeException {

  private static final long serialVersionUID = -2020252584968470396L;

  public ElasticsearchPutMappingException(String index, Exception cause) {
    super(
        "Unable to create mapping in search index " + index +  ".", cause);
  }
}
