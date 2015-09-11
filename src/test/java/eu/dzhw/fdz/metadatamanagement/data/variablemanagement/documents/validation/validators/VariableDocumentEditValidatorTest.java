/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableDocumentRepository;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class VariableDocumentEditValidatorTest extends AbstractWebTest {

  @Autowired
  private VariableDocumentEditValidator editValidator;

  @Autowired
  private VariableDocumentRepository variableRepository;

  @Test
  public void testValidateUniqueVariableAliasNullDocument() {
    // Arrange
    VariableDocument variableDocument = null;

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.editValidator.validateUniqueVariableAlias(variableDocument, errors);

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
    this.editValidator.validateUniqueVariableAlias(variableDocument, errors);

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
    this.editValidator.validateUniqueVariableAlias(variableDocument, errors);

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
    this.editValidator.validateUniqueVariableAlias(variableDocument, errors);

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
    this.editValidator.validateUniqueVariableAlias(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testValidateUniqueVariableAliasUnchanged() {
    // Arrange
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("SurveyID").withVariableAlias("Alias").build();
    VariableDocument variableDocument = new VariableDocumentBuilder()
        .withVariableSurvey(variableSurvey).withId("testValidateUniqueVariableAlias").build();

    // Act
    this.variableRepository.save(variableDocument);
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.editValidator.validateUniqueVariableAlias(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());

    // Delete
    this.variableRepository.delete(variableDocument.getId());
  }

  @Test
  public void testValidateUniqueVariableAliasChangedOkay() {
    // Arrange
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("SurveyID").withVariableAlias("Alias").build();
    VariableDocument variableDocument = new VariableDocumentBuilder()
        .withVariableSurvey(variableSurvey).withId("testValidateUniqueVariableAlias").build();

    // Act
    this.variableRepository.save(variableDocument);
    variableDocument.getVariableSurvey().setVariableAlias("Alias2");
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.editValidator.validateUniqueVariableAlias(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());

    // Delete
    this.variableRepository.delete(variableDocument.getId());
  }

  @Test
  public void testValidateUniqueVariableAliasChangedNotOkay() {
    // Arrange
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("SurveyID").withVariableAlias("Alias").build();
    VariableDocument variableDocument = new VariableDocumentBuilder()
        .withVariableSurvey(variableSurvey).withId("testValidateUniqueVariableAlias").build();
    VariableSurvey variableSurvey2 =
        new VariableSurveyBuilder().withSurveyId("SurveyID").withVariableAlias("Alias2").build();
    VariableDocument variableDocument2 = new VariableDocumentBuilder()
        .withVariableSurvey(variableSurvey2).withId("testValidateUniqueVariableAlias2").build();

    // Act
    this.variableRepository.save(variableDocument);
    this.variableRepository.save(variableDocument2);
    variableDocument.getVariableSurvey().setVariableAlias("Alias2");// changed to second variable
                                                                    // document
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.editValidator.validateUniqueVariableAlias(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(
        errors.getFieldError(VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD)
            .getCode(),
        is(VariableDocumentEditValidator.MANDATORY_VARIABLE_SURVEY_VARIABLEALIAS_MESSAGE_CODE));

    // Delete
    this.variableRepository.delete(variableDocument.getId());
    this.variableRepository.delete(variableDocument2.getId());
  }  
}
