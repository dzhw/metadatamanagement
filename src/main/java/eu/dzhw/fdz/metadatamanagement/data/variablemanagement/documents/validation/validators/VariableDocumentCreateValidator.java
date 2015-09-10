package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Create;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details.VariableResource;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details.VariableResourceAssembler;

/**
 * This is the validator for create a variable document.
 * 
 * @author Daniel Katzberg
 *
 */
@Component(value = "VariableDocumentCreateValidator")
public class VariableDocumentCreateValidator extends VariableDocumentValidator {

  public static final String UNIQUE_ID_VARIABLE_DOCUMENT_ID = "UniqueId.variableDocument.id";

  @Autowired
  public VariableDocumentCreateValidator(@Qualifier("mvcValidator") Validator jsrValidator,
      DataTypesProvider dataTypesProvider, VariableRepository variableRepository,
      VariableResourceAssembler variableResourceAssembler) {
    super(jsrValidator, dataTypesProvider, variableRepository, variableResourceAssembler);
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators.
   * VariableDocumentValidator#getValidationHints()
   */
  @Override
  public Object[] getValidationHints() {
    Object[] validateHints = {Create.class};
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
    {
      // A check for null is not necessary, because it is a type annotation. it can only be checked
      // by
      // initialized objects
      if (variableDocument == null) {
        return;
      }

      if (variableDocument.getVariableSurvey() == null) {
        return;
      }

      // Both are not empty fields -> no valid if there are null
      // but it return valid, because of the NotBlank field. That handles null fields.
      if (variableDocument.getVariableSurvey().getSurveyId() == null
          || variableDocument.getVariableSurvey().getVariableAlias() == null) {
        return;
      }

      rejectDuplicateAliasIfNecessary(variableDocument, errors);
    }
  }

  @Override
  public void validate(Object target, Errors errors) {
    super.validate(target, errors);

    validateUniqueId((VariableDocument) target, errors);
  }

  private void validateUniqueId(VariableDocument variableDocument, Errors errors) {

    // id is a mandatory field
    // but the jsr annotation NotBlank handles the case of no input
    if (variableDocument.getId() == null) {
      return;
    }

    VariableDocument existingVariable = this.variableRepository.findOne(variableDocument.getId());
    // check id
    if (existingVariable == null) {
      return;
    } else {
      VariableResource variableResource = variableResourceAssembler.toResource(variableDocument);
      errors.rejectValue(VariableDocument.ID_FIELD, UNIQUE_ID_VARIABLE_DOCUMENT_ID, new Object[] {
          variableDocument.getId(), variableResource.getId().getHref()}, "FDZ Id already exists!");
      return;
    }
  }


}
