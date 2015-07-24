/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.enumclasses.DataType;
import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.enumclasses.ScaleLevel;
import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator.ValueLabelWrapper;

/**
 * @author Daniel Katzberg
 *
 */
// TODO prepare unit tests for the variable
public class VariableTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(VariableTest.class);

  private static Validator validator;

  @BeforeClass
  public static void setUpValidatorAndDates() {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @Test
  public void emptyVariable() {

    Variable variable = new Variable();
    variable.setFdzId("");
    Set<ConstraintViolation<Variable>> variableVialations = validator.validate(variable);

    // two empty field
    assertEquals(2, variableVialations.size());

    // check for the correct error
    for (ConstraintViolation<Variable> variableVialation : variableVialations) {

      LOGGER.debug("emptyVariable()" + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals("{org.hibernate.validator.constraints.NotEmpty.message}",
          variableVialation.getMessageTemplate());
    }

    variable.setFdzId("ThisIDisOkay.");
    variable.setName("ThisNameIsOkay.");

    variableVialations = validator.validate(variable);

    // now is everything okay
    assertEquals(0, variableVialations.size());

  }

  @Test
  public void tooLongNameTest() {

    Variable variable = new Variable();
    variable.setFdzId("ThisIDisOkay.");
    variable.setName("ThisNameIsTooLongAndThrowAnException.");
    Set<ConstraintViolation<Variable>> variableVialations = validator.validate(variable);

    // one field for the name is too long error
    assertEquals(1, variableVialations.size());

    for (ConstraintViolation<Variable> variableVialation : variableVialations) {

      LOGGER.debug("tooLongNameTest()" + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals("{javax.validation.constraints.Size.message}",
          variableVialation.getMessageTemplate());
    }

    variable.setName("ThisNameIsOkay.");

    variableVialations = validator.validate(variable);

    // now is everything okay
    assertEquals(0, variableVialations.size());
  }

  @Test
  public void tooLongLabelTest() {

    Variable variable = new Variable();
    variable.setFdzId("ThisIDisOkay.");
    variable.setName("ThisNameIsOkay.");
    variable.setLabel("ThisLabelIsNotOkay.ItIsTooLongAndThrowsAnException."
        + "ButTheLabelLengthIsVeryLong.ItNeedsManyWordsForTheException.");
    Set<ConstraintViolation<Variable>> variableVialations = validator.validate(variable);

    // one field for the name is too long error
    assertEquals(1, variableVialations.size());

    for (ConstraintViolation<Variable> variableVialation : variableVialations) {

      LOGGER.debug("tooLongLabelTest()" + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals("{javax.validation.constraints.Size.message}",
          variableVialation.getMessageTemplate());
    }

    variable.setLabel("ThisLabelIsOkay.");
    variableVialations = validator.validate(variable);

    // now is everything okay
    assertEquals(0, variableVialations.size());
  }

  @Test
  public void enumFieldTest() {

    Variable variable = new Variable();
    variable.setFdzId("ThisIDisOkay.");
    variable.setName("ThisNameIsOkay.");
    variable.setDataType(DataType.STRING);
    variable.setScaleLevel(ScaleLevel.ORDINAL);
    Set<ConstraintViolation<Variable>> variableVialations = validator.validate(variable);

    // no errors. enum fields are okay
    assertEquals(0, variableVialations.size());
  }

  @Test
  public void valueTest() {

    Variable variable = new Variable();
    variable.setFdzId("ThisIDisOkay.");
    variable.setName("ThisNameIsOkay.");

    Value value = new Value();
    value.setValues(new ArrayList<>());
    value.setValueLabels(new ArrayList<>());
    value.getValueLabels().add(new ValueLabelWrapper("AddAExtraLabelForAnTestValidationError."));

    variable.setValue(value);
    Set<ConstraintViolation<Variable>> variableVialations = validator.validate(variable);

    // one field for the name is too long error
    assertEquals(1, variableVialations.size());

    for (ConstraintViolation<Variable> variableVialation : variableVialations) {

      LOGGER.debug("ValueTest() " + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals(
          "{eu.dzhw.fdz.metadatamanagement.domain."
              + "variablemanagement.validator.annotations.ValidValueListSize.message}",
          variableVialation.getMessageTemplate());
    }

    value.getValues().add("AGenericValue");
    variableVialations = validator.validate(variable);

    // now is everything okay
    assertEquals(0, variableVialations.size());
  }

  //TODO prepare changes on the listsizeannotation
//  @Test
//  public void valueLabelTest() {
//
//    Variable variable = new Variable();
//    variable.setFdzId("ThisIDisOkay.");
//    variable.setName("ThisNameIsOkay.");
//
//    Value value = new Value();
//    value.setValues(new ArrayList<>());
//    value.setValueLabels(new ArrayList<>());
//    value.getValues().add("AGenericValue");
//    value.getValueLabels().add(new ValueLabelWrapper(
//        "ThisLabelIsNotOkay.ThisLabelIsTooLong.TheLimitIsSmallerThanTheLengthOfThisStringButThisIsATest."));
//
//    variable.setValue(value);
//    Set<ConstraintViolation<Variable>> variableVialations = validator.validate(variable);
//
//    // one field for the name is too long error
//    assertEquals(1, variableVialations.size());
//
//    for (ConstraintViolation<Variable> variableVialation : variableVialations) {
//
//      LOGGER.debug("ValueLabelTest()" + variableVialation.getMessageTemplate() + " -> "
//          + variableVialation.getMessage());
//
//      assertEquals("{javax.validation.constraints.Size.message}",
//          variableVialation.getMessageTemplate());
//    }
//
//    value.getValueLabels().remove(0);
//    value.getValueLabels().add(new ValueLabelWrapper("ThisValueIsOkay."));
//
//    variableVialations = validator.validate(variable);
//
//    // now is everything okay
//    assertEquals(0, variableVialations.size());
//  }

  @Test
  public void variableSurveyForSingleStringTest() {

    Variable variable = new Variable();
    variable.setFdzId("ThisIDisOkay.");
    variable.setName("ThisNameIsOkay.");

    VariableSurvey variableSurvey = new VariableSurvey();

    variable.setVariableSurvey(variableSurvey);
    Set<ConstraintViolation<Variable>> variableVialations = validator.validate(variable);

    // one field for the name is too long error
    assertEquals(2, variableVialations.size());

    for (ConstraintViolation<Variable> variableVialation : variableVialations) {

      LOGGER.debug("variableSurveyTest()" + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals("{org.hibernate.validator.constraints.NotEmpty.message}",
          variableVialation.getMessageTemplate());
    }

    variable.getVariableSurvey().setSurveyId("SurveyIdIsTooLong.ItWillThrowAnException");
    variable.getVariableSurvey().setTitle("TitleIsOkay.");
    variableVialations = validator.validate(variable);

    // one field for the survey id is too long error
    assertEquals(1, variableVialations.size());

    for (ConstraintViolation<Variable> variableVialation : variableVialations) {

      LOGGER.debug("variableSurveyTest()" + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals("{javax.validation.constraints.Size.message}",
          variableVialation.getMessageTemplate());
    }

    variable.getVariableSurvey().setSurveyId("SurveyIdIsOkay.");
    variable.getVariableSurvey().setTitle("TitleIsNotOkay.TheTitleIsTooLong.");
    variableVialations = validator.validate(variable);

    // one field for the title is too long error
    assertEquals(1, variableVialations.size());

    for (ConstraintViolation<Variable> variableVialation : variableVialations) {

      LOGGER.debug("variableSurveyTest()" + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals("{javax.validation.constraints.Size.message}",
          variableVialation.getMessageTemplate());
    }
    variable.getVariableSurvey().setTitle("TitleIsOkay.");

    variableVialations = validator.validate(variable);

    // now is everything okay
    assertEquals(0, variableVialations.size());
  }


  @Test
  public void variableSurveyDateRangeTest() {

    Variable variable = new Variable();
    variable.setFdzId("ThisIDisOkay.");
    variable.setName("ThisNameIsOkay.");

    VariableSurvey variableSurvey = new VariableSurvey();
    variableSurvey.setSurveyId("SurveyIdIsOkay.");
    variableSurvey.setTitle("TitleIsOkay.");

    DateRange dateRange = new DateRange();
    dateRange.setStartDate(LocalDate.now());
    dateRange.setEndDate(dateRange.getStartDate().minusDays(2));
    variableSurvey.setDateRange(dateRange);

    variable.setVariableSurvey(variableSurvey);
    Set<ConstraintViolation<Variable>> variableVialations = validator.validate(variable);

    // one field for the name is too long error
    assertEquals(1, variableVialations.size());

    for (ConstraintViolation<Variable> variableVialation : variableVialations) {

      LOGGER.debug("variableSurveyTest()" + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals(
          "{eu.dzhw.fdz.metadatamanagement.domain.variablemanagement."
              + "validator.annotations.ValidDateRange.message}",
          variableVialation.getMessageTemplate());
    }    
    variable.getVariableSurvey().getDateRange().setEndDate(dateRange.getStartDate().plusDays(2));
    
    
     variableVialations = validator.validate(variable);
    
     // now is everything okay
     assertEquals(0, variableVialations.size());
  }

}
