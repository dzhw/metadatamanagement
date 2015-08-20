/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.DateRangeBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.AnswerOptionBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.UniqueVariableAlias;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.VariableDocumentValidator;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class VariableDocumentTest extends AbstractWebTest {

  @Autowired
  private VariableDocumentValidator variableDocumentValidator;

  @Test
  public void testEmptyInValidVariableDocument() {

    // Arrange
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("").build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(3, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.NAME_FIELD).getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(errors.getFieldError(VariableDocument.QUESTION_FIELD).getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(errors.getFieldError(VariableDocument.ID_FIELD).getCode(),
        is(NotBlank.class.getSimpleName()));
  }

  @Test
  public void testEmptyValidVariableDocument() {

    // Arrange
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testInvalidID() {

    // Assert
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("This ID is not Okay")
        .withName("This Name Is Okay.").withQuestion("DefaultQuestion?").build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.ID_FIELD).getCode(),
        is(Pattern.class.getSimpleName()));
  }

  @Test
  public void testValidIDWithSigns() {

    // Assert
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("This-ID_is-okay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testInvalidName() {

    // Assert
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsTooLongAndThrowAnException.").withQuestion("DefaultQuestion?").build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.NAME_FIELD).getCode(),
        is(Size.class.getSimpleName()));
  }

  @Test
  public void testInvalidLabel() {

    // Assert
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withLabel("ThisLabelIsNotOkay.ItIsTooLongAndThrowsAnException."
                + "ButTheLabelLengthIsVeryLong.ItNeedsManyWordsForTheException.")
        .withQuestion("DefaultQuestion?").build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.LABEL_FIELD).getCode(),
        is(Size.class.getSimpleName()));
  }

  @Test
  public void testValidLabel() {

    // Assert
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withQuestion("DefaultQuestion?").withLabel("ThisLabelIsOkay").build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testInvalidLabelAtAnswerOption() {

    // Arrange
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions.add(new AnswerOptionBuilder()
        .withLabel("AddAExtraLabelForAnTestValidationError.AddAExtraLabelForAnTestValidationError.")
        .build());

    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withQuestion("DefaultQuestion?").withAnswerOptions(answerOptions).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Arrange
    assertEquals(2, errors.getErrorCount());
    assertThat(errors.getFieldError("answerOptions[0].label").getCode(),
        is(Size.class.getSimpleName()));
    assertThat(errors.getFieldError("answerOptions[0].code").getCode(),
        is(NotNull.class.getSimpleName()));


  }

  @Test
  public void testInvalidCodeAtAnswerOption() {

    // Arrange
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions
        .add(new AnswerOptionBuilder().withLabel("This label is okay.").withCode(null).build());

    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withQuestion("DefaultQuestion?").withAnswerOptions(answerOptions).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError("answerOptions[0].code").getCode(),
        is(NotNull.class.getSimpleName()));
  }

  @Test
  public void testValidAnswerOption() {

    // Arrange
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions
        .add(new AnswerOptionBuilder().withLabel("This label is okay.").withCode(5).build());

    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withQuestion("DefaultQuestion?").withAnswerOptions(answerOptions).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
    assertEquals("This label is okay.", variableDocument.getAnswerOptions().get(0).getLabel());
    assertEquals(Integer.valueOf(5), variableDocument.getAnswerOptions().get(0).getCode());
  }

  @Test
  public void testInvalidAnswerOptionWithANullCode() {

    // Arrange
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions
        .add(new AnswerOptionBuilder().withLabel("This label is okay.").withCode(5).build());
    answerOptions
        .add(new AnswerOptionBuilder().withLabel("This label is okay.").withCode(null).build());
    answerOptions
        .add(new AnswerOptionBuilder().withLabel("This label is okay.").withCode(2).build());

    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withQuestion("DefaultQuestion?").withAnswerOptions(answerOptions).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError("answerOptions[1].code").getCode(),
        is(NotNull.class.getSimpleName()));
  }

  @Test
  public void testInvalidVariableDocumentSurveyWithEmptyFields() {

    // Arrange
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?")
        .withVariableSurvey(new VariableSurveyBuilder().build()).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(5, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.NESTED_VARIABLE_SURVEY_ID_FIELD).getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(errors.getFieldError(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD).getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(errors.getFieldError(VariableDocument.NESTED_VARIABLE_SURVEY_PERIOD_FIELD).getCode(),
        is(NotNull.class.getSimpleName()));
    assertThat(errors.getFieldError(VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD)
        .getCode(), is(NotBlank.class.getSimpleName()));
    assertThat(errors.getFieldError(VariableDocument.VARIABLE_SURVEY_FIELD).getCode(),
        is(UniqueVariableAlias.class.getSimpleName()));
  }

  @Test
  public void testInvalidVariableDocumentSurveyWithInvalidAlias() {

    // Arrange
    VariableSurvey variableSurvey = new VariableSurveyBuilder().withSurveyId("SurveyIdIsOkay")
        .withTitle("TitleIsOkay").withVariableAlias("ThisAliasIsTooLong.ItWillThrowAnException")
        .withSurveyPeriod(new DateRangeBuilder().build()).build();

    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withQuestion("DefaultQuestion?").withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD)
        .getCode(), is(Size.class.getSimpleName()));
  }

  @Test
  public void testInvalidVariableDocumentSurveyWithInvalidSurveyId() {

    // Arrange
    VariableSurvey variableSurvey = new VariableSurveyBuilder()
        .withSurveyId("SurveyIdIsTooLong.ItWillThrowAnException").withTitle("TitleIsOkay")
        .withVariableAlias("TitleIsOkay.").withSurveyPeriod(new DateRangeBuilder().build()).build();

    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withQuestion("DefaultQuestion?").withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.NESTED_VARIABLE_SURVEY_ID_FIELD).getCode(),
        is(Size.class.getSimpleName()));
  }


  @Test
  public void testInvalidVariableDocumentSurveyWithInvalidTitle() {

    // Arrange
    VariableSurvey variableSurvey = new VariableSurveyBuilder().withSurveyId("SurveyIdIsOkay.")
        .withTitle("TitleIsNotOkay.TheTitleIsTooLong.").withVariableAlias("ThisNameIsOkay")
        .withSurveyPeriod(new DateRangeBuilder().build()).build();

    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withQuestion("DefaultQuestion?").withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD).getCode(),
        is(Size.class.getSimpleName()));
  }


  @Test
  public void testValidVariableDocumentSurvey() {

    // Arrange
    VariableSurvey variableSurvey = new VariableSurveyBuilder().withSurveyId("SurveyIdIsOkay.")
        .withTitle("TitleIsOkay.").withVariableAlias("ThisNameIsOkay")
        .withSurveyPeriod(new DateRangeBuilder().build()).build();

    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withQuestion("DefaultQuestion?").withVariableSurvey(variableSurvey).build();
    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testVariableDocumentToString() {
    // Arrange
    VariableDocument variableDocument = new VariableDocumentBuilder().build();

    // Act

    // Assert
    assertEquals(
        "VariableDocument [variableSurvey=null, name=null, dataType=null, label=null, scaleLevel=null, answerOptions=[]]",
        variableDocument.toString());
  }

  @Test
  public void testVariableDocumentWithSurvayToString() {
    // Arrange
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withAnswerOptions(new ArrayList<>())
            .withVariableSurvey(new VariableSurveyBuilder().build()).build();

    // Act

    // Assert
    assertEquals(
        "VariableDocument [variableSurvey=VariableSurvey [surveyId=null, title=null, surveyPeriod=null, "
            + "variableAlias=null], name=null, dataType=null, label=null, scaleLevel=null, answerOptions=[]]",
        variableDocument.toString());
  }

  @Test
  public void testVariableDocumentWithSurveyAndEmptyDateRangeToString() {
    // Arrange
    VariableDocument variableDocument = new VariableDocumentBuilder()
        .withAnswerOptions(new ArrayList<>())
        .withVariableSurvey(
            new VariableSurveyBuilder().withSurveyPeriod(new DateRangeBuilder().build()).build())
        .build();

    // Act

    // Assert
    assertEquals(
        "VariableDocument [variableSurvey=VariableSurvey [surveyId=null, title=null, "
            + "surveyPeriod=DateRange [startDate=null, endDate=null], variableAlias=null], name=null, "
            + "dataType=null, label=null, scaleLevel=null, answerOptions=[]]",
        variableDocument.toString());
  }


  @Test
  public void testVariableDocumentWithSurveyAndFilledDateRangeToString() {
    // Arrange
    VariableDocument variableDocument = new VariableDocumentBuilder()
        .withAnswerOptions(new ArrayList<>())
        .withVariableSurvey(new VariableSurveyBuilder().withSurveyPeriod(new DateRangeBuilder()
            .withStartDate(LocalDate.of(2015, 1, 1)).withEndDate(LocalDate.of(2015, 2, 1)).build())
            .build())
        .build();

    // Act

    // Assert
    assertEquals(
        "VariableDocument [variableSurvey=VariableSurvey [surveyId=null, title=null, "
            + "surveyPeriod=DateRange [startDate=2015-01-01, endDate=2015-02-01], variableAlias=null], "
            + "name=null, dataType=null, label=null, scaleLevel=null, answerOptions=[]]",
        variableDocument.toString());
  }

  @Test
  public void testInvalidVariableDocumentWithEmptyQuestion() {
    // Arrange
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.").build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.QUESTION_FIELD).getCode(),
        is(NotBlank.class.getSimpleName()));
  }
  
  @Test
  public void testRemoveAnswerOption() {
    // Arrange
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions.add(new AnswerOptionBuilder().withCode(1).withLabel("Label 1").build());
    answerOptions.add(new AnswerOptionBuilder().withCode(2).build());
    answerOptions.add(new AnswerOptionBuilder().withCode(3).withLabel("Label 3").build());
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withAnswerOptions(answerOptions).build();

    // Act
    AnswerOption answerOption1 = variableDocument.removeAnswerOption(1);
    AnswerOption answerOption2 = variableDocument.removeAnswerOption(1);
    AnswerOption answerOption3 = variableDocument.removeAnswerOption(4);
    AnswerOption answerOption4 = variableDocument.removeAnswerOption(-10);

    // Assert
    assertEquals(new Integer("2"), answerOption1.getCode());
    assertEquals(new Integer("3"), answerOption2.getCode());
    assertEquals("Label 3", answerOption2.getLabel());
    assertEquals(1, variableDocument.getAnswerOptions().size());
    assertEquals(null, answerOption3);
    assertEquals(null, answerOption4);
  }

  @Test
  public void testAddAnswerOption() {
    // Arrange
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").build();

    // Act
    boolean checkNull = variableDocument.addAnswerOption(null);
    boolean checkValidCompleteAnswerOption = variableDocument
        .addAnswerOption(new AnswerOptionBuilder().withCode(1).withLabel("Label").build());
    boolean checkEmptyAnswerOption = variableDocument
        .addAnswerOption(new AnswerOptionBuilder().build());

    // Assert
    assertEquals(false, checkNull);
    assertEquals(true, checkValidCompleteAnswerOption);
    assertEquals(true, checkEmptyAnswerOption);
  }

  @Test
  public void testHashCode() {
    // Arrange
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").build();
    VariableDocument variableDocument2 =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withQuestion("DefaultQuestion?").withAnswerOptions(null).build();

    // Act

    // Assert
    assertEquals(-36021551, variableDocument.hashCode());
    assertEquals(-923525232, variableDocument2.hashCode());
  }

  @Test
  public void testEquals() {
    // Arrange
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").build();

    VariableDocument variableDocument2 = new VariableDocumentBuilder().build();
    VariableDocument variableDocument3 = new VariableDocumentBuilder().build();

    List<AnswerOption> answerOptions = new ArrayList<>();
    List<AnswerOption> answerOptions2 = new ArrayList<>();
    answerOptions2.add(new AnswerOptionBuilder().withCode(1).withLabel("Label").build());

    VariableSurvey variableSurvey = new VariableSurveyBuilder().withSurveyId("1").build();
    VariableSurvey variableSurvey2 = new VariableSurveyBuilder().withSurveyId("2").build();

    // Act
    boolean checkNull = variableDocument.equals(null);
    boolean checkDifferentClass = variableDocument.equals(new Object());
    boolean checkDifferentVariableDocument = variableDocument.equals(variableDocument2);
    boolean checkDifferentVariableDocumentWithNullName =
        variableDocument3.equals(variableDocument2);
    variableDocument.setAnswerOptions(answerOptions);
    variableDocument2.setAnswerOptions(null);
    boolean checkAnswerOptionsOther = variableDocument2.equals(variableDocument);
    variableDocument2.setAnswerOptions(answerOptions);
    boolean checkAnswerOptionsOtherBoth = variableDocument2.equals(variableDocument);
    variableDocument2.setAnswerOptions(answerOptions2);
    boolean checkAnswerOptionsOtherBothDifferent = variableDocument2.equals(variableDocument);
    variableDocument.setDataType("string");
    variableDocument2.setAnswerOptions(null);
    variableDocument.setAnswerOptions(null);
    boolean checkDifferentDataType = variableDocument2.equals(variableDocument);
    variableDocument2.setDataType("string");
    boolean checkDifferentDataTypeBoth = variableDocument2.equals(variableDocument);
    variableDocument2.setDataType("numeric");
    boolean checkDifferentDataTypeBothDifferent = variableDocument2.equals(variableDocument);
    variableDocument.setDataType(null);
    variableDocument2.setDataType(null);
    variableDocument.setLabel("Label 1");
    boolean checkLabelOther = variableDocument2.equals(variableDocument);
    variableDocument2.setLabel("Label 2");
    boolean checkLabelBoth = variableDocument2.equals(variableDocument);
    variableDocument2.setLabel("Label 1");
    boolean checkLabelBothSame = variableDocument2.equals(variableDocument);
    variableDocument.setLabel(null);
    variableDocument2.setLabel(null);
    variableDocument2.setName("Another Name");
    boolean checkDifferentName = variableDocument2.equals(variableDocument);
    variableDocument2.setName(variableDocument.getName());
    boolean checkSameName = variableDocument2.equals(variableDocument);
    variableDocument2.setName(null);
    variableDocument.setName(null);
    variableDocument.setQuestion("DefaultQuestion?");
    boolean checkQuestionOther = variableDocument2.equals(variableDocument);
    variableDocument2.setQuestion("AnotherDefaultQuestion?");
    boolean checkQuestionBoth = variableDocument2.equals(variableDocument);
    variableDocument2.setQuestion(variableDocument.getQuestion());
    boolean checkQuestionBothSame = variableDocument2.equals(variableDocument);
    variableDocument.setQuestion(null);
    variableDocument2.setQuestion(null);
    variableDocument.setScaleLevel("nominal");
    boolean checkScaleLevelOther = variableDocument2.equals(variableDocument);
    variableDocument2.setScaleLevel("metric");
    boolean checkScaleLevelBoth = variableDocument2.equals(variableDocument);
    variableDocument2.setScaleLevel(variableDocument.getScaleLevel());
    boolean checkScaleLevelBothSame = variableDocument2.equals(variableDocument);
    variableDocument.setScaleLevel(null);
    variableDocument2.setScaleLevel(null);
    variableDocument.setVariableSurvey(variableSurvey);
    boolean checkVariableSurveyOther = variableDocument2.equals(variableDocument);
    variableDocument2.setVariableSurvey(variableSurvey2);
    boolean checkVariableSurveyBoth = variableDocument2.equals(variableDocument);
    variableDocument2.setVariableSurvey(variableSurvey);
    boolean checkVariableSurveyBothSame = variableDocument2.equals(variableDocument);

    // Assert
    assertEquals(false, checkNull);
    assertEquals(false, checkDifferentClass);
    assertEquals(false, checkDifferentVariableDocument);
    assertEquals(true, checkDifferentVariableDocumentWithNullName);
    assertEquals(false, checkAnswerOptionsOther);
    assertEquals(false, checkAnswerOptionsOtherBoth);
    assertEquals(false, checkAnswerOptionsOtherBothDifferent);
    assertEquals(false, checkDifferentDataType);
    assertEquals(false, checkDifferentDataTypeBoth);
    assertEquals(false, checkDifferentDataTypeBothDifferent);
    assertEquals(false, checkLabelOther);
    assertEquals(false, checkLabelBoth);
    assertEquals(false, checkLabelBothSame);
    assertEquals(false, checkDifferentName);
    assertEquals(false, checkSameName);
    assertEquals(false, checkQuestionOther);
    assertEquals(false, checkQuestionBoth);
    assertEquals(true, checkQuestionBothSame);
    assertEquals(false, checkScaleLevelOther);
    assertEquals(false, checkScaleLevelBoth);
    assertEquals(true, checkScaleLevelBothSame);
    assertEquals(false, checkVariableSurveyOther);
    assertEquals(false, checkVariableSurveyBoth);
    assertEquals(true, checkVariableSurveyBothSame);
  }
}
