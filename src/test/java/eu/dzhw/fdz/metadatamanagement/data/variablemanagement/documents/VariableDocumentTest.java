/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

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

/**
 * @author Daniel Katzberg
 *
 */
public class VariableDocumentTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(VariableDocumentTest.class);

  private static Validator VALIDATOR;

  @BeforeClass
  public static void setUpValidatorAndDates() {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    VALIDATOR = validatorFactory.getValidator();
  }

  @Test
  public void testEmptyInValidVariableDocument() {

    // Arrange
    VariableDocument variable = new VariableDocument();
    variable.setId("");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations = VALIDATOR.validate(variable);

    // Assert
    assertEquals(2, variableVialations.size());
    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testEmptyInValidVariable()" + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals("{org.hibernate.validator.constraints.NotEmpty.message}",
          variableVialation.getMessageTemplate());
    }
  }

  @Test
  public void testEmptyValidVariableDocument() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(0, variableVialations.size());
  }

  @Test
  public void testInvalidID() {

    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("This ID is not okay.");
    variableDocument.setName("This name is okay.");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(1, variableVialations.size());
    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testInvalidID()" + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals("{javax.validation.constraints.Pattern.message}",
          variableVialation.getMessageTemplate());
    }
  }

  @Test
  public void testValidIDWithSigns() {

    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("This-ID_is-okay");
    variableDocument.setName("This name is okay.");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(0, variableVialations.size());
  }

  @Test
  public void testInvalidName() {

    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsTooLongAndThrowAnException.");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(1, variableVialations.size());
    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testInvalidName()" + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals("{javax.validation.constraints.Size.message}",
          variableVialation.getMessageTemplate());
    }
  }

  @Test
  public void testInvalidLabel() {

    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    variableDocument.setLabel("ThisLabelIsNotOkay.ItIsTooLongAndThrowsAnException."
        + "ButTheLabelLengthIsVeryLong.ItNeedsManyWordsForTheException.");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(1, variableVialations.size());
    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testInvalidLabel()" + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals("{javax.validation.constraints.Size.message}",
          variableVialation.getMessageTemplate());
    }
  }

  @Test
  public void testValidLabel() {

    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    variableDocument.setLabel("ThisLabelIsOkay.");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(0, variableVialations.size());
  }

  @Test
  public void testValidDataField() {
    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    variableDocument.setDataType("sTrinG");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(0, variableVialations.size());
  }

  @Test
  public void testInvalidDataField() {
    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    variableDocument.setDataType("sTrinGIsNotOkay");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(1, variableVialations.size());
    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testInvalidDataField() " + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals(
          "{eu.dzhw.fdz.metadatamanagement.data.variablemanagement."
              + "documents.enum.validation.validdatatype.message}",
          variableVialation.getMessageTemplate());
    }
  }

  @Test
  public void testValidScaleLevel() {

    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    variableDocument.setScaleLevel("oRdiNal");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(0, variableVialations.size());
  }

  @Test
  public void testInvalidScaleLevel() {
    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    variableDocument.setScaleLevel("oRdiNalIsNotOkay");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(1, variableVialations.size());
    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testInvalidDataField() " + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals(
          "{eu.dzhw.fdz.metadatamanagement.data.variablemanagement."
              + "documents.enum.validation.validscalelevel.message}",
          variableVialation.getMessageTemplate());
    }
  }

  @Test
  public void testInvalidLabelAtAnswerOption() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    AnswerOption answerOption = new AnswerOption();
    answerOption
        .setLabel("AddAExtraLabelForAnTestValidationError.AddAExtraLabelForAnTestValidationError.");
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions.add(answerOption);
    variableDocument.setAnswerOptions(answerOptions);

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Arrange
    assertEquals(2, variableVialations.size());
    Iterator<ConstraintViolation<VariableDocument>> ite = variableVialations.iterator();

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
  }


  @Test
  public void testInvalidCodeAtAnswerOption() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    AnswerOption answerOption = new AnswerOption();
    answerOption.setLabel("This label is okay.");
    answerOption
        .setCode("AddAExtraLabelForAnTestValidationError.AddAExtraLabelForAnTestValidationError.");
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions.add(answerOption);
    variableDocument.setAnswerOptions(answerOptions);

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(1, variableVialations.size());
    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testInvalidLabelAtAnswerOption() " + variableVialation.getMessageTemplate()
          + " -> " + variableVialation.getMessage());

      assertEquals("{javax.validation.constraints.Size.message}",
          variableVialation.getMessageTemplate());
    }
  }

  @Test
  public void testValidAnswerOption() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    AnswerOption answerOption = new AnswerOption();
    answerOption.setCode("This code is okay.");
    answerOption.setLabel("Label is okay.");
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions.add(answerOption);
    variableDocument.setAnswerOptions(answerOptions);

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(0, variableVialations.size());
    assertEquals("Label is okay.", variableDocument.getAnswerOptions().get(0).getLabel());
    assertEquals("This code is okay.", variableDocument.getAnswerOptions().get(0).getCode());
  }

  @Test
  public void testInvalidVariableDocumentSurveyWithEmptyFields() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    VariableSurvey variableSurvey = new VariableSurvey();
    variableDocument.setVariableSurveyDocument(variableSurvey);

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(3, variableVialations.size());
    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testInvalidVariableDocumentSurveyWithEmptyFields()"
          + variableVialation.getMessageTemplate() + " -> " + variableVialation.getMessage());

      assertEquals("{org.hibernate.validator.constraints.NotEmpty.message}",
          variableVialation.getMessageTemplate());
    }
  }

  @Test
  public void testInvalidVariableDocumentSurveyWithInvalidAlias() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    VariableSurvey variableSurvey = new VariableSurvey();
    variableDocument.setVariableSurveyDocument(variableSurvey);
    variableDocument.getVariableSurveyDocument().setSurveyId("AliasIsOkay.");
    variableDocument.getVariableSurveyDocument().setTitle("TitleIsOkay.");
    variableDocument.getVariableSurveyDocument()
        .setAlias("ThisAliasIsTooLong.ItWillThrowAnException");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(1, variableVialations.size());

    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testInvalidVariableDocumentSurveyWithInvalidSurveyId()"
          + variableVialation.getMessageTemplate() + " -> " + variableVialation.getMessage());

      assertEquals("{javax.validation.constraints.Size.message}",
          variableVialation.getMessageTemplate());
    }
  }

  @Test
  public void testInvalidVariableDocumentSurveyWithInvalidSurveyId() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    VariableSurvey variableSurvey = new VariableSurvey();
    variableDocument.setVariableSurveyDocument(variableSurvey);
    variableDocument.getVariableSurveyDocument()
        .setSurveyId("SurveyIdIsTooLong.ItWillThrowAnException");
    variableDocument.getVariableSurveyDocument().setTitle("TitleIsOkay.");
    variableDocument.getVariableSurveyDocument().setAlias(variableDocument.getName());

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(1, variableVialations.size());

    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testInvalidVariableDocumentSurveyWithInvalidSurveyId()"
          + variableVialation.getMessageTemplate() + " -> " + variableVialation.getMessage());

      assertEquals("{javax.validation.constraints.Size.message}",
          variableVialation.getMessageTemplate());
    }
  }


  @Test
  public void testInvalidVariableDocumentSurveyWithInvalidTitle() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    VariableSurvey variableSurvey = new VariableSurvey();
    variableDocument.setVariableSurveyDocument(variableSurvey);
    variableDocument.getVariableSurveyDocument().setAlias(variableDocument.getName());
    variableDocument.getVariableSurveyDocument().setSurveyId("SurveyIdIsOkay.");
    variableDocument.getVariableSurveyDocument().setTitle("TitleIsNotOkay.TheTitleIsTooLong.");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(1, variableVialations.size());

    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("variableSurveyTest()" + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals("{javax.validation.constraints.Size.message}",
          variableVialation.getMessageTemplate());
    }
  }


  @Test
  public void testValidVariableDocumentSurvey() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    VariableSurvey variableSurvey = new VariableSurvey();
    variableDocument.setVariableSurveyDocument(variableSurvey);
    variableDocument.getVariableSurveyDocument().setSurveyId("SurveyIdIsOkay.");
    variableDocument.getVariableSurveyDocument().setTitle("TitleIsOkay.");
    variableDocument.getVariableSurveyDocument().setAlias(variableDocument.getName());

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(0, variableVialations.size());
  }


  @Test
  public void testInvalidDateRange() {

    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");

    VariableSurvey variableSurvey = new VariableSurvey();
    variableSurvey.setSurveyId("SurveyIdIsOkay.");
    variableSurvey.setTitle("TitleIsOkay.");
    variableSurvey.setAlias(variableDocument.getName());

    DateRange surveyPeriod = new DateRange();
    surveyPeriod.setStartDate(LocalDate.now());
    surveyPeriod.setEndDate(surveyPeriod.getStartDate().minusDays(2));
    variableSurvey.setSurveyPeriod(surveyPeriod);

    variableDocument.setVariableSurveyDocument(variableSurvey);

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    // assertEquals(1, variableVialations.size());

    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testInvalidDateRange()" + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals(
          "{eu.dzhw.fdz.metadatamanagement.data.variablemanagement."
              + "common.validation.validDateRange.message}",
          variableVialation.getMessageTemplate());
    }
  }

  @Test
  public void testValidDateRange() {

    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");

    VariableSurvey variableSurvey = new VariableSurvey();
    variableSurvey.setSurveyId("SurveyIdIsOkay.");
    variableSurvey.setTitle("TitleIsOkay.");
    variableSurvey.setAlias(variableDocument.getName());

    DateRange surveyPeriod = new DateRange();
    surveyPeriod.setStartDate(LocalDate.now());
    surveyPeriod.setEndDate(surveyPeriod.getStartDate().plusDays(2));
    variableSurvey.setSurveyPeriod(surveyPeriod);

    variableDocument.setVariableSurveyDocument(variableSurvey);

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(0, variableVialations.size());
  }

  @Test
  public void testEmptyDateRangeValidator() {

    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ID");
    variableDocument.setName("name");
    variableDocument.setVariableSurveyDocument(new VariableSurvey());
    variableDocument.getVariableSurveyDocument().setSurveyId("ID_Survey");
    variableDocument.getVariableSurveyDocument().setTitle("TitleIsOkay.");
    variableDocument.getVariableSurveyDocument().setAlias(variableDocument.getName());

    // set a date, but without a start and end date
    variableDocument.getVariableSurveyDocument().setSurveyPeriod(new DateRange());


    Set<ConstraintViolation<VariableDocument>> variableVialations =
        VALIDATOR.validate(variableDocument);

    // everything okay
    assertEquals(0, variableVialations.size());
  }

  @Test
  public void testVariableDocumentToString() {
    // Arrange
    VariableDocument variableDocument = new VariableDocument();

    // Act

    // Assert
    assertEquals(
        "VariableDocument [variableSurvey=null, name=null, dataType=null, "
        + "label=null, scaleLevel=null, answerOptions=null]",
        variableDocument.toString());
  }

  @Test
  public void testVariableDocumentWithSurvayToString() {
    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setVariableSurveyDocument(new VariableSurvey());
    variableDocument.setAnswerOptions(new ArrayList<>());

    // Act

    // Assert
    assertEquals(
        "VariableDocument [variableSurvey=VariableSurvey [surveyId=null, title=null, surveyPeriod=null, "
        + "alias=null], name=null, dataType=null, label=null, scaleLevel=null, answerOptions=[]]",
        variableDocument.toString());
  }

  @Test
  public void testVariableDocumentWithSurveyAndEmptyDateRangeToString() {
    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setVariableSurveyDocument(new VariableSurvey());
    variableDocument.setAnswerOptions(new ArrayList<>());
    variableDocument.getVariableSurveyDocument().setSurveyPeriod(new DateRange());

    // Act

    // Assert
    assertEquals(
        "VariableDocument [variableSurvey=VariableSurvey [surveyId=null, title=null, "
        + "surveyPeriod=DateRange [startDate=null, endDate=null], alias=null], name=null, "
        + "dataType=null, label=null, scaleLevel=null, answerOptions=[]]",
        variableDocument.toString());
  }


  @Test
  public void testVariableDocumentWithSurveyAndFilledDateRangeToString() {
    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setVariableSurveyDocument(new VariableSurvey());
    variableDocument.setAnswerOptions(new ArrayList<>());
    variableDocument.getVariableSurveyDocument().setSurveyPeriod(new DateRange());
    variableDocument.getVariableSurveyDocument().getSurveyPeriod()
        .setStartDate(LocalDate.of(2015, 1, 1));
    variableDocument.getVariableSurveyDocument().getSurveyPeriod()
        .setEndDate(LocalDate.of(2015, 2, 1));

    // Act

    // Assert
    assertEquals(
        "VariableDocument [variableSurvey=VariableSurvey [surveyId=null, title=null, "
        + "surveyPeriod=DateRange [startDate=2015-01-01, endDate=2015-02-01], alias=null], "
        + "name=null, dataType=null, label=null, scaleLevel=null, answerOptions=[]]",
        variableDocument.toString());
  }
}
