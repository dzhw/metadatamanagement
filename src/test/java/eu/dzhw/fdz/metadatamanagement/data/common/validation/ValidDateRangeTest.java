/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.common.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.common.validation.ValidDateRange;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.VariableDocumentValidator;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class ValidDateRangeTest extends AbstractWebTest {
  
  @Autowired
  private VariableDocumentValidator variableDocumentValidator;
  
  @Test
  public void testInvalidDateRange() {

    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    variableDocument.setQuestion("DefaultQuestion?");

    VariableSurvey variableSurvey = new VariableSurvey();
    variableSurvey.setSurveyId("SurveyIdIsOkay.");
    variableSurvey.setTitle("TitleIsOkay.");
    variableSurvey.setVariableAlias(variableDocument.getName());

    DateRange surveyPeriod = new DateRange();
    surveyPeriod.setStartDate(LocalDate.now());
    surveyPeriod.setEndDate(surveyPeriod.getStartDate().minusDays(2));
    variableSurvey.setSurveyPeriod(surveyPeriod);

    variableDocument.setVariableSurvey(variableSurvey);

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.NESTED_VARIABLE_SURVEY_PERIOD_FIELD).getCode(),
        is(ValidDateRange.class.getSimpleName()));
  }

  @Test
  public void testValidDateRange() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    variableDocument.setQuestion("DefaultQuestion?");

    VariableSurvey variableSurvey = new VariableSurvey();
    variableSurvey.setSurveyId("SurveyIdIsOkay.");
    variableSurvey.setTitle("TitleIsOkay.");
    variableSurvey.setVariableAlias(variableDocument.getName());

    DateRange surveyPeriod = new DateRange();
    surveyPeriod.setStartDate(LocalDate.now());
    surveyPeriod.setEndDate(surveyPeriod.getStartDate().plusDays(2));
    variableSurvey.setSurveyPeriod(surveyPeriod);

    variableDocument.setVariableSurvey(variableSurvey);

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testEmptyDateRangeValidator() {
    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ID");
    variableDocument.setName("name");
    variableDocument.setQuestion("DefaultQuestion?");
    variableDocument.setVariableSurvey(new VariableSurvey());
    variableDocument.getVariableSurvey().setSurveyId("ID_Survey");
    variableDocument.getVariableSurvey().setTitle("TitleIsOkay.");
    variableDocument.getVariableSurvey().setVariableAlias(variableDocument.getName());
    variableDocument.getVariableSurvey().setSurveyPeriod(new DateRange());

    // set a date, but without a start and end date
    variableDocument.getVariableSurvey().setSurveyPeriod(new DateRange());

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

}
