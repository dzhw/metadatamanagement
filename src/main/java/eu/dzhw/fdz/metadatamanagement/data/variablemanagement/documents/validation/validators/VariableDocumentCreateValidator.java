package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Create;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;

/**
 * This is the validator for create a variable document.
 * 
 * @author Daniel Katzberg
 *
 */
@Component(value = "VariableDocumentCreateValidator")
public class VariableDocumentCreateValidator extends VariableDocumentValidator {

  @Autowired
  public VariableDocumentCreateValidator(@Qualifier("mvcValidator") Validator jsrValidator,
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
    Object[] validateHints = {Create.class};
    return validateHints;
  }

}
