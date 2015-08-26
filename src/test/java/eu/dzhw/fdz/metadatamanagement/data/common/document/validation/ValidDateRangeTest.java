/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.common.document.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.DateRangeBuilder;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.ValidDateRange;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators.VariableDocumentCreateValidator;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class ValidDateRangeTest extends AbstractWebTest {

  @Autowired
  private VariableDocumentCreateValidator variableDocumentCreateValidator;

  @Test
  public void testInvalidDateRange() {

    // Arrange
    DateRange surveyPeriod = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().minusDays(2)).build();

    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("SurveyIdIsOkay").withTitle("TitleIsOkay")
            .withVariableAlias("ThisNameIsOkay").withSurveyPeriod(surveyPeriod).build();

    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("LabelisOkay")
        .withLabel("LabelisOkay").withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.NESTED_VARIABLE_SURVEY_PERIOD_FIELD).getCode(),
        is(ValidDateRange.class.getSimpleName()));
  }

  @Test
  public void testValidDateRange() {

    // Arrange
    DateRange surveyPeriod = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();

    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("SurveyIdIsOkay").withTitle("TitleIsOkay")
            .withVariableAlias("ThisNameIsOkay").withSurveyPeriod(surveyPeriod).build();

    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("LabelisOkay")
        .withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testEqualStartDateAndEndDate() {

    // Arrange
    DateRange surveyPeriod =
        new DateRangeBuilder().withStartDate(LocalDate.now()).withEndDate(LocalDate.now()).build();

    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("SurveyIdIsOkay").withTitle("TitleIsOkay")
            .withVariableAlias("ThisNameIsOkay").withSurveyPeriod(surveyPeriod).build();

    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("LabelisOkay")
        .withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
  }

  @Test
  public void testEmptyDateRangeValidator() {
    // Arrange
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("SurveyIdIsOkay").withTitle("TitleIsOkay")
            .withVariableAlias("name").withSurveyPeriod(new DateRangeBuilder().build()).build();

    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ID").withName("name").withQuestion("DefaultQuestion?")
            .withLabel("LabelisOkay").withVariableSurvey(variableSurvey).build();

    // set a date, but without a start and end date
    variableDocument.getVariableSurvey().setSurveyPeriod(new DateRange());

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

}
