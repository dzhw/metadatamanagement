package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Edit;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * This is the validator for editing the variable document.
 * 
 * @author Daniel Katzberg
 *
 */
@Component(value = "VariableDocumentEditValidator")
public class VariableDocumentEditValidator extends VariableDocumentValidator {

  @Autowired
  public VariableDocumentEditValidator(@Qualifier("mvcValidator") Validator jsrValidator,
      DataTypesProvider dataTypesProvider) {
    super(jsrValidator, dataTypesProvider);
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators.
   * VariableDocumentValidator#getValidateHints()
   */
  @Override
  public Object[] getValidateHints() {
    Object[] validateHints = {Edit.class};
    return validateHints;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators.
   * VariableDocumentValidator#validateUniqueVariableAlias(eu.dzhw.fdz.metadatamanagement.data.
   * variablemanagement.documents.VariableDocument, org.springframework.validation.Errors)
   */
  @Override
  protected void validateUniqueVariableAlias(VariableDocument variableDocument, Errors errors) {
    // TODO DKatzberg: Prepare impl.
  }

}
