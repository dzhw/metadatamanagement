package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ValidResponse;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Validates the valid responses of a variable. It checks the values has a numeric string, if the
 * variable has a numeric data type.
 * 
 * @author Daniel Katzberg
 *
 */
public class ValidResponseValueMustBeAnIsoDateOnDateDataTypeValidator implements
    ConstraintValidator<ValidResponseValueMustBeAnIsoDateOnDateDataType, Variable> {

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(
      ValidResponseValueMustBeAnIsoDateOnDateDataType constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Variable variable, ConstraintValidatorContext context) {

    if (variable == null) {
      return true;
    }

    if (variable.getDataType() == null) {
      return true;
    }

    if (variable.getDistribution() == null) {
      return true;
    }

    if (variable.getDistribution().getValidResponses() == null) {
      return true;
    }

    if (variable.getDataType()
        .equals(DataTypes.DATE)) {
      for (ValidResponse validResponse : variable.getDistribution().getValidResponses()) {
        // if one value is not iso standard ... send a false.

        try {
          LocalDate.parse(validResponse.getValue());
          return true;
        } catch (DateTimeParseException dtpe) {
          return false; // not parsable
        }
      }
    }

    // no date, everything is okay.
    return true;
  }

}
