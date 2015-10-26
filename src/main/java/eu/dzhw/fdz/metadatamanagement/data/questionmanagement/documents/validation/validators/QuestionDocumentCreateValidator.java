package eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.validation.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Create;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionDocument;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.repositories.QuestionDocumentRepository;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details.QuestionResource;
import eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details.QuestionResourceAssembler;

/**
 * This is the validator for create a variable document.
 * 
 * @author Daniel Katzberg
 *
 */
@Component(value = "QuestionDocumentCreateValidator")
public class QuestionDocumentCreateValidator extends QuestionDocumentValidator {

  public static final String UNIQUE_ID_VARIABLE_DOCUMENT_ID = "UniqueId.variableDocument.id";

  @Autowired
  public QuestionDocumentCreateValidator(@Qualifier("mvcValidator") Validator jsrValidator,
      QuestionDocumentRepository questionDocumentRepository,
      QuestionResourceAssembler questionResourceAssembler) {
    super(jsrValidator, questionDocumentRepository, questionResourceAssembler);
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
   * @see eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.validation.validators.
   * QuestionDocumentValidator#validate(java.lang.Object, org.springframework.validation.Errors)
   */
  @Override
  public void validate(Object target, Errors errors) {
    super.validate(target, errors);

    validateUniqueId((QuestionDocument) target, errors);
  }

  private void validateUniqueId(QuestionDocument questionDocument, Errors errors) {

    // id is a mandatory field
    // but the jsr annotation NotBlank handles the case of no input
    if (questionDocument.getId() == null) {
      return;
    }

    QuestionDocument existingQuestion =
        this.questionDocumentRepository.findOne(questionDocument.getId());
    // check id
    if (existingQuestion == null) {
      return;
    } else {
      QuestionResource questionResource =
          this.questionResourceAssembler.toResource(questionDocument);
      errors.rejectValue(VariableDocument.ID_FIELD.getAbsolutePath(),
          UNIQUE_ID_VARIABLE_DOCUMENT_ID,
          new Object[] {questionDocument.getId(), questionResource.getId().getHref()},
          "FDZ Id already exists!");
      return;
    }
  }


}
