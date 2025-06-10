package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

/**
 * Exception that is thrown if the DataCiteMetadata cannot be created.
 */
public class DataCiteMetadataException extends Exception {
  private static final long serialVersionUID = 648251203132962140L;

  public DataCiteMetadataException(String message) {
    super(message);
  }
}
