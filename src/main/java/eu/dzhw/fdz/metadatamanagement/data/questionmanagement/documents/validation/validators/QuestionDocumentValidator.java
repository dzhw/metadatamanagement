package eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.validation.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.validator.AbstractCommonValidator;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionDocument;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.repositories.QuestionDocumentRepository;
import eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details.QuestionResourceAssembler;

/**
 * A custom spring validator for the variable document. It uses the default JSR-303 validator and
 * additionally checks if the scale level is valid depending on the current datatype.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public abstract class QuestionDocumentValidator extends AbstractCommonValidator {

  protected QuestionDocumentRepository questionDocumentRepository;
  protected QuestionResourceAssembler questionResourceAssembler;

  /**
   * The variable document validator is abstract validator which defines the default implementation
   * of the edit and create validators.
   * 
   * @param jsrValidator A given JSR 303 Validator
   * @param questionDocumentRepository the repository class of the question document.
   * @param questionResourceAssembler the assembler of the question resource, which includes a
   *        question document
   */
  public QuestionDocumentValidator(Validator jsrValidator,
      QuestionDocumentRepository questionDocumentRepository,
      QuestionResourceAssembler questionResourceAssembler) {
    super(jsrValidator);
    this.questionDocumentRepository = questionDocumentRepository;
    this.questionResourceAssembler = questionResourceAssembler;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.validation.Validator#supports(java.lang.Class)
   */
  @Override
  public boolean supports(Class<?> clazz) {
    return clazz.equals(QuestionDocument.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.validation.Validator#validate(java.lang.Object,
   * org.springframework.validation.Errors)
   */
  @Override
  public void validate(Object target, Errors errors) {
    // first execute JSR-303 validation
    this.jsrValidator.validate(target, errors, this.getValidationHints());
  }
}
