/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.enums.DataType;
import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.enums.ScaleLevel;

/**
 * @author Daniel Katzberg
 *
 */
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
  public void answerOptionsTest() {

    Variable variable = new Variable();
    variable.setFdzId("ThisIDisOkay.");
    variable.setName("ThisNameIsOkay.");


    AnswerOption answerOption = new AnswerOption();
    answerOption
        .setCode("AddAExtraLabelForAnTestValidationError.AddAExtraLabelForAnTestValidationError.");

    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions.add(answerOption);

    variable.setAnswerOptions(answerOptions);
    Set<ConstraintViolation<Variable>> variableVialations = validator.validate(variable);

    // one field for the name is too long error
    // needed label
    assertEquals(2, variableVialations.size());
    Iterator<ConstraintViolation<Variable>> ite = variableVialations.iterator();

    // both messages found (it is not possible to expect an exact order of error messages
    boolean foundSize = false;
    boolean foundNotEmpty = false;
    while (ite.hasNext()) {
      String msgTemplate = ite.next().getMessageTemplate();
      if (msgTemplate.equals("{javax.validation.constraints.Size.message}") && !foundSize) {
        foundSize = true;
      }
      if (msgTemplate.equals("{org.hibernate.validator.constraints.NotEmpty.message}")
          && !foundNotEmpty) {
        foundNotEmpty = true;
      }
    }
    assertEquals(true, foundSize);
    assertEquals(true, foundNotEmpty);

    // code is okay, label too long
    variable.getAnswerOptions().get(0)
        .setLabel("AddAExtraLabelForAnTestValidationError.AddAExtraLabelForAnTestValidationError.");
    variable.getAnswerOptions().get(0).setCode("This code is okay.");
    variableVialations = validator.validate(variable);

    // one field for the name is too long error
    assertEquals(1, variableVialations.size());

    for (ConstraintViolation<Variable> variableVialation : variableVialations) {

      LOGGER.debug("ValueTest() " + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals("{javax.validation.constraints.Size.message}",
          variableVialation.getMessageTemplate());
    }

    variable.getAnswerOptions().get(0).setLabel("AddAExtraLabelForAnTestValidationError");
    variableVialations = validator.validate(variable);

    // now is everything okay
    assertEquals(0, variableVialations.size());
    assertEquals("AddAExtraLabelForAnTestValidationError",
        variable.getAnswerOptions().get(0).getLabel());
    assertEquals("This code is okay.", variable.getAnswerOptions().get(0).getCode());
  }

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

  @Test
  public void toStringTest() {
    // Empty Variable
    Variable variable = new Variable();
    assertEquals(
        "Survey [fdzId=null, null, name=null, dataType=null, label=null, scaleLevel=null, AnswerOptions.size=0]",
        variable.toString());

    // Variable with VariableSurvey
    variable.setVariableSurvey(new VariableSurvey());
    assertEquals("Survey [fdzId=null, SurveyVariableSurvey [surveyId=null, "
        + "title=null, null], name=null, dataType=null, label=null, "
        + "scaleLevel=null, AnswerOptions.size=0]", variable.toString());

    // Variable Survey with empty RangeDate
    variable.getVariableSurvey().setDateRange(new DateRange());;
    assertEquals(
        "Survey [fdzId=null, SurveyVariableSurvey [surveyId=null, "
            + "title=null, DateRange [StartDate=null, EndDate=null]], name=null, "
            + "dataType=null, label=null, scaleLevel=null, AnswerOptions.size=0]",
        variable.toString());
  }
}
