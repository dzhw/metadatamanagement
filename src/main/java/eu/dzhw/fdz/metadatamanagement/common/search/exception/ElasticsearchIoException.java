package eu.dzhw.fdz.metadatamanagement.common.search.exception;

import java.io.IOException;

/**
 * Exception which is to be thrown when any elasticsearch operation cannot be performed due to IO
 * problems.
 * 
 * @author René Reitmann
 */
public class ElasticsearchIoException extends RuntimeException {
  
  private static final long serialVersionUID = -509508603503231633L;

  public ElasticsearchIoException(IOException ioException) {
    super("Unable to connect to elasticsearch.", ioException);
  }
}
