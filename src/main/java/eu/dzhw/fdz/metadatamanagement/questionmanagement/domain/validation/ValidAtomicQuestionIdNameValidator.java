package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.AtomicQuestion;

/**
 * Validates the name of a id. The pattern is: DataAcquisitionProjectId-VariableName. This validator
 * validates only until the minus. The variable name is not possible for a validation on upload.
 * 
 * @author Daniel Katzberg
 *
 */
public class ValidAtomicQuestionIdNameValidator
    implements ConstraintValidator<ValidAtomicQuestionIdName, AtomicQuestion> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidAtomicQuestionIdName constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(AtomicQuestion atomicQuestion, ConstraintValidatorContext context) {
    return atomicQuestion.getId()
      .startsWith(atomicQuestion.getDataAcquisitionProjectId() + "-")
        && atomicQuestion.getId()
          .equals(atomicQuestion.getVariableId());
  }
}
