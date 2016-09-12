package eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;

/**
 * Ensure that a study id is equal to its project id.
 * 
 * @author René Reitmann
 */
public class ValidStudyIdValidator implements ConstraintValidator<ValidStudyId, Study> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidStudyId constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Study study, ConstraintValidatorContext context) {
    return study.getId().equals(study.getDataAcquisitionProjectId());
  }
}
