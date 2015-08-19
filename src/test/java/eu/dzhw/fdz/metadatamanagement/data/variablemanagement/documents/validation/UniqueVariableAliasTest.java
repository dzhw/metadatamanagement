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

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
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
    VariableDocument variableDocument1 = new VariableDocument();
    variableDocument1.setId("ThisIDisOkay");
    variableDocument1.setName("ThisNameIsOkay.");
    variableDocument1.setQuestion("DefaultQuestion?");

    VariableSurvey variableSurvey1 = new VariableSurvey();
    variableSurvey1.setSurveyId("SurveyIdIsOkay.");
    variableSurvey1.setTitle("TitleIsOkay.");
    variableSurvey1.setVariableAlias(variableDocument1.getName());
    variableSurvey1.setSurveyPeriod(new DateRange());

    variableDocument1.setVariableSurvey(variableSurvey1);
    this.repository.save(variableDocument1);

    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay2");
    variableDocument.setName("ThisNameIsOkay2.");
    variableDocument.setQuestion("DefaultQuestion?");

    VariableSurvey variableSurvey = new VariableSurvey();
    variableSurvey.setSurveyId("SurveyIdIsOkay.");
    variableSurvey.setTitle("TitleIsOkay.");
    variableSurvey.setSurveyPeriod(new DateRange());
    // here is the error, double used alias
    variableSurvey.setVariableAlias(variableDocument1.getName());

    variableDocument.setVariableSurvey(variableSurvey);

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.VARIABLE_SURVEY_FIELD).getCode(),
        is("UniqueVariableAlias"));

    // Delete
    this.repository.delete(variableDocument.getId());
  }


  @Test
  public void testValidVariableSecondDocumentSurvey() {

    // Arrange
    VariableDocument variableDocument1 = new VariableDocument();
    variableDocument1.setId("ThisIDisOkay");
    variableDocument1.setName("ThisNameIsOkay.");
    variableDocument1.setQuestion("DefaultQuestion?");

    VariableSurvey variableSurvey1 = new VariableSurvey();
    variableSurvey1.setSurveyId("SurveyIdIsOkay.");
    variableSurvey1.setTitle("TitleIsOkay.");
    variableSurvey1.setVariableAlias(variableDocument1.getName());
    variableSurvey1.setSurveyPeriod(new DateRange());

    variableDocument1.setVariableSurvey(variableSurvey1);
    this.repository.save(variableDocument1);

    VariableDocument variableDocument2 = new VariableDocument();
    variableDocument2.setId("ThisIDisOkay2");
    variableDocument2.setName("ThisNameIsOkay2.");
    variableDocument2.setQuestion("DefaultQuestion?");

    VariableSurvey variableSurvey2 = new VariableSurvey();
    variableSurvey2.setSurveyId("SurveyIdIsOkay.");
    variableSurvey2.setTitle("TitleIsOkay.");
    variableSurvey2.setVariableAlias(variableDocument2.getName());
    variableSurvey2.setSurveyPeriod(new DateRange());

    variableDocument2.setVariableSurvey(variableSurvey2);

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument2, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument2, errors);

    // Assert
    assertEquals(0, errors.getFieldErrorCount());

    // Delete
    this.repository.delete(variableDocument1.getId());
  }

  @Test
  public void testUniqueAliasWithNullSurvey() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    variableDocument.setQuestion("DefaultQuestion?");
    variableDocument.setVariableSurvey(null);

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }
}
