package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetTypes;

/**
 * Validator for the type of a data set. Only valued from the {@link DataSetTypes} class are
 * allowed.
 *
 */
public class ValidDataSetTypeValidator implements 
             ConstraintValidator<ValidDataSetType, I18nString> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidDataSetType constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString dataSetType, ConstraintValidatorContext context) {

    // check for data set types
    return DataSetTypes.ALL.contains(dataSetType);
  }

}
