package eu.dzhw.fdz.metadatamanagement.web.common.dtos;

import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object which serializes springs validation results as json.
 * 
 * @author René Reitmann
 */
public class ValidationResultDto {

  /**
   * A Map fieldName -> List of internationalized error messages. FieldName may be 'global' as well.
   */
  private Map<String, List<String>> errorMessageMap;

  public Map<String, List<String>> getErrorMessageMap() {
    return this.errorMessageMap;
  }

  public void setErrorMessageMap(Map<String, List<String>> errorMessageMap) {
    this.errorMessageMap = errorMessageMap;
  }

}
