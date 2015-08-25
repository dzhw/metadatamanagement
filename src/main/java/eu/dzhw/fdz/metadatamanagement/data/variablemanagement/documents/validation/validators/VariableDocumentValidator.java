package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * A custom spring validator for the variable document. It uses the default JSR-303 validator and
 * additionally checks if the scale level is valid depending on the current datatype.
 * 
 * @author René Reitmann
 */
public abstract class VariableDocumentValidator implements Validator {

  public static final String MANDATORY_SCALE_LEVEL_MESSAGE_CODE =
      "MandatoryScaleLevelOnNumericDataType.variabledocument.scaleLevel";

  private SmartValidator jsrValidator;

  private DataTypesProvider dataTypesProvider;

  public abstract Object[] getValidateHints();

  /**
   * Yeah.
   * 
   * @param jsrValidator More yeah.
   * @param dataTypesProvider More yeah.
   */
  public VariableDocumentValidator(@Qualifier("mvcValidator") Validator jsrValidator,
      DataTypesProvider dataTypesProvider) {
    if (jsrValidator instanceof SmartValidator) {
      this.jsrValidator = (SmartValidator) jsrValidator;
    } else {
      throw new RuntimeException(
          "Cast not successful at validators... (should be a smart validator.)");
    }
    this.dataTypesProvider = dataTypesProvider;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return clazz.equals(VariableDocument.class);
  }

  @Override
  public void validate(Object target, Errors errors) {
    // first execute JSR-303 validation
    this.jsrValidator.validate(target, errors, this.getValidateHints());

    VariableDocument variableDocument = (VariableDocument) target;

    // check if the scale level is correct
    validateMandatoryScaleLevelOnNumericDataType(variableDocument, errors);
  }

  private void validateMandatoryScaleLevelOnNumericDataType(VariableDocument variableDocument,
      Errors errors) {
    // check for null
    if (variableDocument.getDataType() == null) {
      return;
    }

    // register an error if the datatype is numeric and scale level is not set
    if (variableDocument.getDataType().equals(this.dataTypesProvider.getNumericValueByLocale())
        && variableDocument.getScaleLevel() == null) {
      errors.rejectValue(VariableDocument.SCALE_LEVEL_FIELD, MANDATORY_SCALE_LEVEL_MESSAGE_CODE);
      return;
    }
  }
}
