package eu.dzhw.fdz.metadatamanagement.common.service;

/**
 * Thrown if a GridFS file with the same name already exists.
 */
public class DuplicateFilenameException extends RuntimeException {

  private static final long serialVersionUID = -162875553599970465L;

  /**
   * Create a new exception.
   * @param filename Name of the duplicate file
   */
  public DuplicateFilenameException(String filename) {
    super("A file named " + filename + " already exists");
  }
}
