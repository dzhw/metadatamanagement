package eu.dzhw.fdz.metadatamanagement.datasetmanagement.exception;

import java.util.List;

/**
 * This exception handles the case of incomplete data set report tex template zip files.
 * @author dkatzberg
 *
 */
public class TemplateIncompleteException extends Exception {

  private static final long serialVersionUID = 1L;
  
  private List<String> missingFiles;
  
  public TemplateIncompleteException(String message, List<String> missingFiles) {
    super(message);
    this.missingFiles = missingFiles;
  }

  /* GETTER / SETTER */
  public List<String> getMissingFiles() {
    return missingFiles;
  }

  public void setMissingFiles(List<String> missingFiles) {
    this.missingFiles = missingFiles;
  }
}
