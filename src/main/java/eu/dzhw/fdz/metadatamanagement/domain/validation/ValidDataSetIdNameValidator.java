package eu.dzhw.fdz.metadatamanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.domain.DataSet;

/**
 * Validates the name of a id. The pattern is: DataAcquisitionProjectId-ds{Number}. This validator
 * validates the complete name.
 * 
 * @author dkatzberg
 *
 */
public class ValidDataSetIdNameValidator
    implements ConstraintValidator<ValidDataSetIdName, DataSet> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidDataSetIdName constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(DataSet dataset, ConstraintValidatorContext context) {
    // check for set project id
    if (dataset.getDataAcquisitionProjectId() == null) {
      return false;
    }
    return dataset.getId().matches(dataset.getDataAcquisitionProjectId() + "\\-ds" + "[0-9]*");
  }

}
