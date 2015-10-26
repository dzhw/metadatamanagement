package eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.validator;

import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;

/**
 * A general and abstract implementation of the JSR 303 Validator.
 *  
 * @author Daniel Katzberg
 *
 */
public abstract class AbstractCommonValidator implements Validator {
  
  protected SmartValidator jsrValidator;
  
  /**
   * Constructor for an abstract validator.
   * @param jsrValidator A smart validator for basic 
   */
  public AbstractCommonValidator(Validator jsrValidator) {
    if (jsrValidator instanceof SmartValidator) {
      this.jsrValidator = (SmartValidator) jsrValidator;
    } else {
      throw new IllegalStateException(
          "Cast not successful at validators... (should be a smart validator.)");
    }
  }

  /**
   * Create the validtion hints used by the JSR validator.
   * 
   * @return An array of {@link VariableValidationGroups} classes
   */
  protected abstract Object[] getValidationHints();
  
  

}
