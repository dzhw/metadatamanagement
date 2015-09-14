/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

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
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators.VariableDocumentCreateValidator;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableDocumentRepository;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class UniqueIdValidatorTest extends AbstractWebTest {

  @Autowired
  private VariableDocumentRepository repository;

  @Autowired
  private VariableDocumentCreateValidator variableDocumentCreateValidator;

  @Test
  public void testValidUniqueId() {

    // Arrange
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument1 = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withLabel("LabelIsOkay").withQuestion("DefaultQuestion?")
        .withVariableSurvey(variableSurvey).build();
    this.repository.save(variableDocument1);

    VariableSurvey variableSurvey2 =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 2")
            .withVariableAlias("VariableAlias 2").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument2 = new VariableDocumentBuilder().withId("ThisIDisStillOkay")
        .withName("ThisNameIsOkay.").withLabel("LabelIsOkay").withQuestion("DefaultQuestion?")
        .withVariableSurvey(variableSurvey2).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument2, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument2, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());

    // Delete
    this.repository.delete(variableDocument1.getId());
  }

  @Test
  public void testInvalidUniqueId() {
    // Arrange
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument1 = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withLabel("LabelIsOkay").withQuestion("DefaultQuestion?")
        .withVariableSurvey(variableSurvey).build();
    this.repository.save(variableDocument1);

    VariableSurvey variableSurvey2 =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 2")
            .withVariableAlias("VariableAlias 2").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument2 = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withLabel("LabelIsOkay").withQuestion("DefaultQuestion?")
        .withVariableSurvey(variableSurvey2).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument2, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument2, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.ID_FIELD.getPath()).getCode(),
        is(VariableDocumentCreateValidator.UNIQUE_ID_VARIABLE_DOCUMENT_ID));

    // Delete
    this.repository.delete(variableDocument1.getId());
  }
}
