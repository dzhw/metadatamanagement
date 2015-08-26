/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.AnswerOption;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.AnswerOptionBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
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

    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?")
        .withLabel("LabelIsOkay").withAnswerOptions(answerOptions).build();

    VariableDocument variableDocumentNullAnswerOptions = new VariableDocumentBuilder()
        .withId("ThisIDisOkay").withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?")
        .withLabel("LabelIsOkay").withAnswerOptions(null).build();

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

    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?")
        .withLabel("LabelIsOkay").withAnswerOptions(answerOptions).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.ANSWER_OPTIONS_FIELD).getCode(),
        is("UniqueAnswerCode"));
  }
}
