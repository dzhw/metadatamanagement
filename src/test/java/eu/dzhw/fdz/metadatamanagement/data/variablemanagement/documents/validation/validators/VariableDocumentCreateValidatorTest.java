/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableDocumentRepository;

/**
 * @author Daniel Katzberg
 *
 */
public class VariableDocumentCreateValidatorTest extends AbstractTest {

  @Autowired
  private VariableDocumentCreateValidator createValidator;

  @Autowired
  private VariableDocumentRepository variableRepository;

  @Test
  public void testValidateUniqueVariableAliasNullDocument() {
    // Arrange
    VariableDocument variableDocument = null;

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.createValidator.validateUniqueVariableAlias(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testValidateUniqueVariableAliasNullSurvey() {
    // Arrange
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withVariableSurvey(null).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.createValidator.validateUniqueVariableAlias(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testValidateUniqueVariableAliasNullSurveyId() {
    // Arrange
    VariableSurvey variableSurvey = new VariableSurveyBuilder().withVariableAlias("Alias").build();
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.createValidator.validateUniqueVariableAlias(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testValidateUniqueVariableAliasNullVariableAlias() {
    // Arrange
    VariableSurvey variableSurvey = new VariableSurveyBuilder().withSurveyId("SurveyID").build();
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.createValidator.validateUniqueVariableAlias(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testValidateUniqueVariableAliasNoElementsFound() {
    // Arrange
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("SurveyID").withVariableAlias("Alias").build();
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.createValidator.validateUniqueVariableAlias(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testValidateUniqueVariableAliasElementsFound() {
    // Arrange
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("SurveyID").withVariableAlias("Alias").build();
    VariableDocument variableDocument = new VariableDocumentBuilder()
        .withVariableSurvey(variableSurvey).withId("testValidateUniqueVariableAlias").build();

    // Act
    this.variableRepository.save(variableDocument);
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.createValidator.validateUniqueVariableAlias(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(
        errors
            .getFieldError(
                VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD.getAbsolutePath())
            .getCode(),
        is(VariableDocumentCreateValidator.MANDATORY_VARIABLE_SURVEY_VARIABLEALIAS_MESSAGE_CODE));

    // Delete
    this.variableRepository.delete(variableDocument.getId());
  }

}
