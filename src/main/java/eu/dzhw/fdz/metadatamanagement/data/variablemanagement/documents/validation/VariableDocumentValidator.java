package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * A custom spring validator for the variable document. It current uses the default JSR-303
 * validator and additionally checks if the scale level is valid depending on the current datatype.
 * 
 * @author René Reitmann
 */
@Component
public class VariableDocumentValidator implements Validator {

  public static final String MANDATORY_SCALE_LEVEL_MESSAGE_CODE =
      "MandatoryScaleLevelOnNumericDataType.variabledocument.scaleLevel";

  private Validator jsrValidator;

  private DataTypesProvider dataTypesProvider;

  @Autowired
  public VariableDocumentValidator(@Qualifier("mvcValidator") Validator jsrValidator,
      DataTypesProvider dataTypesProvider) {
    this.jsrValidator = jsrValidator;
    this.dataTypesProvider = dataTypesProvider;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return clazz.equals(VariableDocument.class);
  }

  @Override
  public void validate(Object target, Errors errors) {
    // first execute JSR-303 validation
    jsrValidator.validate(target, errors);

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
