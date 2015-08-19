/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.DateRangeBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class UniqueVariableAliasTest extends AbstractWebTest {

  @Autowired
  private VariableRepository repository;

  @Autowired
  private VariableDocumentValidator variableDocumentValidator;

  @Test
  public void testInvalidVariableSecondDocumentSurvey() {

    // Arrange
    VariableSurvey variableSurvey1 =
        new VariableSurveyBuilder().withSurveyId("SurveyIdIsOkay").withTitle("TitleIsOkay")
            .withVariableAlias("ThisNameIsOkay").withSurveyPeriod(new DateRangeBuilder().build()).build();

    VariableDocument variableDocument1 =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withQuestion("DefaultQuestion?").withVariableSurvey(variableSurvey1).build();
    this.repository.save(variableDocument1);

    // here is the error, double used alias
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("SurveyIdIsOkay").withTitle("TitleIsOkay")
            .withVariableAlias("ThisNameIsOkay").withSurveyPeriod(new DateRangeBuilder().build()).build();

    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisStillOkay").withName("ThisNameIsStillOkay.")
            .withQuestion("DefaultQuestion2?").withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.VARIABLE_SURVEY_FIELD).getCode(),
        is(UniqueVariableAlias.class.getSimpleName()));

    // Delete
    this.repository.delete(variableDocument1.getId());
  }


  @Test
  public void testValidVariableSecondDocumentSurvey() {

    // Arrange
    VariableSurvey variableSurvey1 =
        new VariableSurveyBuilder().withSurveyId("SurveyIdIsOkay").withTitle("TitleIsOkay")
            .withVariableAlias("ThisNameIsOkay").withSurveyPeriod(new DateRangeBuilder().build()).build();

    VariableDocument variableDocument1 =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withQuestion("DefaultQuestion?").withVariableSurvey(variableSurvey1).build();
    this.repository.save(variableDocument1);

    // here is the error, double used alias
    VariableSurvey variableSurvey = new VariableSurveyBuilder().withSurveyId("SurveyIdIsOkay")
        .withTitle("TitleIsOkay").withVariableAlias("ThisNameIsStillOkay")
        .withSurveyPeriod(new DateRangeBuilder().build()).build();

    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisStillOkay").withName("ThisNameIsStillOkay.")
            .withQuestion("DefaultQuestion2?").withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getFieldErrorCount());

    // Delete
    this.repository.delete(variableDocument1.getId());
  }

  @Test
  public void testUniqueAliasWithNullSurvey() {

    // Arrange
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withQuestion("DefaultQuestion?").withVariableSurvey(null).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }
}
