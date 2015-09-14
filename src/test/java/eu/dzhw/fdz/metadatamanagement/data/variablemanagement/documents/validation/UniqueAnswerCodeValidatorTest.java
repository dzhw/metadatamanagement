/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.DateRangeBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.AnswerOption;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.AnswerOptionBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators.VariableDocumentCreateValidator;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class UniqueAnswerCodeValidatorTest extends AbstractWebTest {

  @Autowired
  private VariableDocumentCreateValidator variableDocumentCreateValidator;

  @Test
  public void testValidAnswerCode() {

    // Arrange
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions.add(new AnswerOptionBuilder().withCode(1).withLabel("Label 1").build());
    answerOptions.add(new AnswerOptionBuilder().withCode(2).withLabel("Label 2").build());

    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withVariableSurvey(variableSurvey).withQuestion("DefaultQuestion?")
            .withLabel("LabelIsOkay").withAnswerOptions(answerOptions).build();

    VariableSurvey variableSurvey2 =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 2")
            .withVariableAlias("VariableAlias 2").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocumentNullAnswerOptions =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withQuestion("DefaultQuestion?").withLabel("LabelIsOkay")
            .withVariableSurvey(variableSurvey2).withAnswerOptions(null).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);
    Errors errorsNullAnswerOptions =
        new BeanPropertyBindingResult(variableDocumentNullAnswerOptions, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocumentNullAnswerOptions, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
    assertEquals(0, errorsNullAnswerOptions.getErrorCount());
  }

  @Test
  public void testInvalidAnswerCode() {

    // Arrange
    List<AnswerOption> answerOptions = new ArrayList<>();
    // Invalid of the same code = 1
    answerOptions.add(new AnswerOptionBuilder().withCode(1).withLabel("Label 1").build());
    answerOptions.add(new AnswerOptionBuilder().withCode(1).withLabel("Label 2").build());

    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("LabelIsOkay")
        .withAnswerOptions(answerOptions).withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.ANSWER_OPTIONS_FIELD.getPath()).getCode(),
        is("UniqueAnswerCode"));
  }
}
